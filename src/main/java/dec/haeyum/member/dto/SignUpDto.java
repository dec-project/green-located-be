package dec.haeyum.member.dto;

import dec.haeyum.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignUpDto {
    private String username;
    private String password;

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .roles(roles)
                .build();
    }
}
