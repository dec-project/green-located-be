package dec.haeyum.chat;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.member.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())
                || StompCommand.SUBSCRIBE.equals(accessor.getCommand())
                || StompCommand.SEND.equals(accessor.getCommand())) {
            String accessToken = validateToken(accessor);
            log.info("Authentication success");

            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            accessor.setUser(authentication);

            log.info("command, user: {}, {}",accessor.getCommand(), accessor.getUser());
        }

        return message;
    }

    private String validateToken(StompHeaderAccessor accessor) {
        String accessToken = jwtTokenProvider.resolveAccessTokenStomp(accessor);
        if (!(StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken))) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        return accessToken;
    }
}
