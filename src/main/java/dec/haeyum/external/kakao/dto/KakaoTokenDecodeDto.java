package dec.haeyum.external.kakao.dto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenDecodeDto {

    private String aud;
    private String sub;
    private Integer authTime;
    private String iss;
    private Integer exp;
    private Integer iat;
    private String nickname;
    private String picture;
    private String email;

    public KakaoTokenDecodeDto(String idToken) {
        DecodedJWT decode = JWT.decode(idToken);
        this.aud = decode.getClaim("aud").asString();
        this.sub = decode.getClaim("sub").asString();
        this.authTime = decode.getClaim("authTime").asInt();
        this.iss = decode.getClaim("iss").asString();
        this.exp = decode.getClaim("exp").asInt();
        this.iat = decode.getClaim("iat").asInt();
        this.nickname = decode.getClaim("nickname").asString();
        this.picture = decode.getClaim("picture").asString();
        this.email = decode.getClaim("email").asString();
    }

}
