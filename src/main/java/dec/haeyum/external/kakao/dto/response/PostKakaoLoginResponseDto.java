package dec.haeyum.external.kakao.dto.response;

import dec.haeyum.member.dto.JwtToken;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostKakaoLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String socialSub;

    public PostKakaoLoginResponseDto(JwtToken jwtToken, String sub) {
        this.accessToken = jwtToken.getAccessToken();
        this.refreshToken = jwtToken.getRefreshToken();
        this.socialSub = sub;
    }

    public static ResponseEntity<PostKakaoLoginResponseDto> success(JwtToken jwtToken, String sub) {
        PostKakaoLoginResponseDto postKakaoLoginResponseDto = new PostKakaoLoginResponseDto(jwtToken, sub);
        return ResponseEntity.ok().body(postKakaoLoginResponseDto);
    }

}
