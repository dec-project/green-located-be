package dec.haeyum.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostUpdateProfileRequestDto {
    @NotNull
    private MultipartFile profileImg;
    @NotNull(message = "이름은 필수 입력 입니다.")
    @Size(min = 2,max = 10, message = "닉네임은 최소 {min}자 이상, 최대 {max}자 이하이여야 합니다.")
    private String nickname;


}
