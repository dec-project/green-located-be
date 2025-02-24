package dec.haeyum.external.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoTokenInfoError {

    private String error;
    private String error_description;
    private String error_code;

}
