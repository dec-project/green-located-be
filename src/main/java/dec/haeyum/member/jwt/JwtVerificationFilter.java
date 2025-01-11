package dec.haeyum.member.jwt;

import dec.haeyum.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

    //인증에서 제외할 url
    private static final List<String> EXCLUDE_URL =
            List.of("/sign-in",
                    "/sign-up",
                    "/search/**");
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header에서 JWT 토큰 추출
        String accessToken = jwtTokenProvider.resloveAccessToken(request);

        // 2. 토큰 유효성 검사
        if (StringUtils.hasText(accessToken) && doNotLogout(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            setAuthenticationToContext(accessToken);
        }
        filterChain.doFilter(request, response);
    }

    // 로그아웃 상태인지 확인
    private boolean doNotLogout(String accessToken) {
        String isLogout = redisService.getValues(accessToken);
        return isLogout.equals("false");    // accessToken이 "false"이면(return값 true) 블랙리스트에 올라온 상태가 아니므로 로그아웃되지 않음
    }

    // 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
    private void setAuthenticationToContext(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("# Token verification success!");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }


}
