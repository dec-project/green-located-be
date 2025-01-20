package dec.haeyum.member.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostRefreshResponseDto {

    private String accessToken;

    public PostRefreshResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static ResponseEntity<PostRefreshResponseDto> success(String accessToken) {
        PostRefreshResponseDto result = new PostRefreshResponseDto(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
