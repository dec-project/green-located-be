package dec.haeyum.external.kakao.dto.response;

import dec.haeyum.member.dto.JwtToken;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class PostKakaoLoginResponseDtoV2 {

    private String accessToken;
    private String social_sub;


    public PostKakaoLoginResponseDtoV2(JwtToken jwtToken, String sub) {
        this.accessToken = jwtToken.getAccessToken();
        this.social_sub = sub;
    }

    public static ResponseEntity<PostKakaoLoginResponseDtoV2> succeess(JwtToken jwtToken, String sub) {
        PostKakaoLoginResponseDtoV2 result = new PostKakaoLoginResponseDtoV2(jwtToken, sub);

        return ResponseEntity.ok().body(result);
    }
}
