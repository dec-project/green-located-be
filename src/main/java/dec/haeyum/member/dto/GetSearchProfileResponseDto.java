package dec.haeyum.member.dto;

import dec.haeyum.member.entity.Member;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetSearchProfileResponseDto {


    private String profileImg;
    private String nickname;

    public GetSearchProfileResponseDto(String fileUrl, Member member) {
        this.profileImg = fileUrl + member.getProfileImg();
        this.nickname = member.getUsername();
    }


    public static ResponseEntity<GetSearchProfileResponseDto> success(String fileUrl, Member member) {
        GetSearchProfileResponseDto result = new GetSearchProfileResponseDto(fileUrl, member);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
