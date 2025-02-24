package dec.haeyum.external.kakao.dto.request;

import dec.haeyum.external.kakao.dto.response.PostKakaoLoginResponseDtoV2;
import dec.haeyum.member.dto.JwtToken;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class PostKakaoLoginRequestDtoV2 {


    private String accessToken;
    private String idToken;


}
