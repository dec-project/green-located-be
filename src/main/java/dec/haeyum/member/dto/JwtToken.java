package dec.haeyum.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {
    String accessToken;
    String refreshToken;
    String grantType;
}
