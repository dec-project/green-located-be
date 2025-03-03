package dec.haeyum.member.jwt;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.member.entity.Member;
import dec.haeyum.redis.RedisService;
import dec.haeyum.social.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService{

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialService socialService;

    @Override
    public ResponseEntity<PostRefreshResponseDto> refreshAccessToken() {
        log.info("updateAccessToken::start");
        // 들어온 refreshToken 을 parse 해서 유저 식별
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("sub ={} ",sub);
        // 레디스 조회 -> value 하고 일치하면 해당 유저 accessToken 만들어주기
        String isRefreshToken = redisService.getRefreshTokenInString(sub);
        log.info("isRefreshToken = {}", isRefreshToken);
        if (isRefreshToken == null || isRefreshToken.isEmpty()){
            log.info("ERROR TOKEN");
            throw new BusinessException(ErrorCode.EXPIRED_TOKNE);
        }
        Member member = socialService.findMember(sub);
        List<String> roles = member.getRoles();
        String accessToken = jwtTokenProvider.generateAccessTokenWithKakao(sub, roles);
        log.info("new AccessToken = {}",accessToken);
        return PostRefreshResponseDto.success(accessToken);
    }
}
