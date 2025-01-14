package dec.haeyum.external.kakao.dto.response;

import lombok.Getter;

@Getter
public class KakaoAuthorizeResponseDto {

    // 토큰 받기에 필요한 인가 코드
    private String code;
    // 인증 실패 시 반환되는 에러 코드
    private String error;
    private String error_description;
    // 요청 시 전달한 state 값과 동일한 값
    private String state;


}
