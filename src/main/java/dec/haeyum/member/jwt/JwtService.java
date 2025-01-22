package dec.haeyum.member.jwt;

import org.springframework.http.ResponseEntity;

public interface JwtService {
    ResponseEntity<PostRefreshResponseDto> refreshAccessToken();
}
