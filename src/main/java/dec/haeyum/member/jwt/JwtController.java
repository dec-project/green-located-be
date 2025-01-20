package dec.haeyum.member.jwt;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.dto.PostRefreshRequestDto;
import dec.haeyum.member.entity.Member;
import dec.haeyum.redis.RedisService;
import dec.haeyum.social.service.SocialService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialService socialService;

    @Value("${front.url}")
    private String frontUrl;

    @GetMapping("/refresh")
    public ResponseEntity<PostRefreshResponseDto> updateAccessToken(HttpServletResponse response){
        String accessToken = "";
        log.info("updateAccessToken::start");
        try {
            // 들어온 refreshToken 을 parse 해서 유저 식별
            String sub = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("sub ={} ",sub);
            // 레디스 조회 -> value 하고 일치하면 해당 유저 accessToken 만들어주기
            String isRefreshToken = redisService.getRefreshTokenInString(sub);
            if (isRefreshToken == null || isRefreshToken.isEmpty()){
                //response.sendRedirect( frontUrl + "/oauth/kakao/login");
                throw new BusinessException(ErrorCode.TEST);
            }
            Member member = socialService.findMember(sub);
            List<String> roles = member.getRoles();
            accessToken = jwtTokenProvider.generateAccessTokenWithKakao(sub, roles);

        }catch (Exception e){
            e.printStackTrace();
        }
        return PostRefreshResponseDto.success(accessToken);
    }
}
