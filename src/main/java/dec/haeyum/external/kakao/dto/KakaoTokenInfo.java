package dec.haeyum.external.kakao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class KakaoTokenInfo {

    // 성공 응답
    @Schema(description = "ID 토큰을 발급한 인증 기관 정보")
    private String iss;
    @Schema(description = "ID 토큰이 발급된 앱 키")
    private String aud;
    @Schema(description = "ID 토큰에 해당하는 사용자의 회원 번호")
    private String sub;
    @Schema(description = "ID 토큰 발급 또는 갱신 시간")
    private Integer iat;
    @Schema(description = "ID 토큰 만료 시간")
    private Integer exp;
    @Schema(description = "로그인 인증을 완료한 시간")
    private Integer auth_time;
    @Schema(description = "사용자 카카오 닉네임")
    private String nickname;
    @Schema(description = "사용자 카카오 사진")
    private String picture;
    @Schema(description = "사용자 카카오 이메일")
    private String email;

}
