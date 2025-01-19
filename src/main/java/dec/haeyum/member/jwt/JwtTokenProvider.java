package dec.haeyum.member.jwt;

import dec.haeyum.member.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    @Getter
    @Value("${jwt.access-token-expiration-millis}")
    private long accessTokenExpirationMillis;

    @Getter
    @Value("${jwt.refresh-token-expiration-millis}")
    private long refreshTokenExpirationMillis;


    // application.yml에서 secret 값 가져와서 key에 저장
    public  JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 생성
    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();

        //Access Token 생성
        Date accessTokenExpiresln = new Date(now + accessTokenExpirationMillis);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresln)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(now + refreshTokenExpirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 생성
    public JwtToken generateTokenWithKakao(String sub, List<String> roles) {

        String authorities = roles.stream()
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        //Access Token 생성
        Date accessTokenExpiresln = new Date(now + accessTokenExpirationMillis);
        String accessToken = Jwts.builder()
                .setSubject(sub)
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresln)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(sub)
                .claim("auth", authorities)
                .setExpiration(new Date(now + refreshTokenExpirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 복호화
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰 정보 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT Claims string is empty", e);
        }
        return false;
    }

    // 토큰에서 Authentication 리턴
    public Authentication getAuthentication(String accessToken) {
        //JWT 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //Claim에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //User 객체를 만들어서 Authentication 리턴
        //UserDetails: interface, User: UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Request Header에서 Access Token 추출
    public String resloveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // STOMP Header에서 Accss Token 추출
    public String resolveAccessTokenStomp(StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // STOMP Header에서 Accss Token 추출
    public String resolveBearerToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
