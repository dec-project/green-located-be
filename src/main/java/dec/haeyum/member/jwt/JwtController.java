package dec.haeyum.member.jwt;

import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.dto.PostRefreshRequestDto;
import dec.haeyum.member.entity.Member;
import dec.haeyum.redis.RedisService;
import dec.haeyum.social.service.SocialService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialService socialService;

    @Value("${front.url}")
    private String frontUrl;

    @PostMapping("/refresh")
    public ResponseEntity<PostRefreshResponseDto> updateAccessToken(HttpServletResponse response){
        String accessToken = "";
        try {

            // 들어온 refreshToken 을 parse 해서 유저 식별
            String sub = SecurityContextHolder.getContext().getAuthentication().getName();
            // 레디스 조회 -> value 하고 일치하면 해당 유저 accessToken 만들어주기
            String isRefreshToken = redisService.getRefreshTokenInString(sub);
            if (isRefreshToken == null || isRefreshToken.isEmpty()){
                response.sendRedirect( frontUrl + "/oauth/kakao/login");
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
