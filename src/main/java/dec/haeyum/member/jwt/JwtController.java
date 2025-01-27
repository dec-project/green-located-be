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

    private final JwtService jwtService;

    @GetMapping("/refresh")
    public ResponseEntity<PostRefreshResponseDto> updateAccessToken(){
        ResponseEntity<PostRefreshResponseDto> result = jwtService.refreshAccessToken();
        return result;
    }

}
