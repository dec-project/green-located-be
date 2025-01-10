package dec.haeyum.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignInDto {
    private String username;
    private String password;
}
