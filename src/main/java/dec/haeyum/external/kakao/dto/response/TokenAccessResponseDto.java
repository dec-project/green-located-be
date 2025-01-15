package dec.haeyum.external.kakao.dto.response;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dec.haeyum.external.kakao.dto.KakaoTokenDecodeDto;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

@Getter
public class TokenAccessResponseDto {

    private String token_type;
    private String access_token;
    private String id_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private String scope;


    public KakaoTokenDecodeDto idTokenDecode(){
        KakaoTokenDecodeDto kakaoTokenDecodeDto = new KakaoTokenDecodeDto(this.id_token);
        return kakaoTokenDecodeDto;

    }

    }
















