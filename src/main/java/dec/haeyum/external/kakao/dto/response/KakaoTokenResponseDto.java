package dec.haeyum.external.kakao.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenResponseDto {

    private String code;
    private String error;
    private String errorDescription;

}
