package dec.haeyum.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

    //인증에서 제외할 url
    private static final List<String> EXCLUDE_URL =
            List.of("/sign-in",
                    "/sign-up",
                    "/weather/img",
                    "/oauth/kakao/login",
                    "/image/**",
                    "/search/**",
                    "/calendar",
                    "/calendar/**",
                    "/view/**",
                    "/favorite/**",
                    "/oauth/kakao/**");
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("requestPath ={} ",request.getServletPath());
        if (shouldNotFilter(request)){
            filterChain.doFilter(request,response);
        }

        // 1. Request Header에서 JWT 토큰 추출
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        // 2. 토큰 유효성 검사
        if (StringUtils.hasText(accessToken) && doNotLogout(accessToken)&& jwtTokenProvider.validateToken(accessToken)) {
            setAuthenticationToContext(accessToken);
            log.info(SecurityContextHolder.getContext().toString());
        }else {
            expiredToken(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static void expiredToken(HttpServletResponse response) throws IOException {
        log.info("expiredToken");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, String> errorDetail = new HashMap<>();
        errorDetail.put("code","ET");
        errorDetail.put("message","다시 로그인해 주시기 바랍니다.");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorDetail));
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
        return EXCLUDE_URL.stream().anyMatch(exclude -> pathMatcher.match(exclude,request.getServletPath()));
    }
}
