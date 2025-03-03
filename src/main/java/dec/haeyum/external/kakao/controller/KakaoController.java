package dec.haeyum.external.kakao.controller;

import dec.haeyum.external.kakao.dto.request.PostKakaoLoginRequestDto;
import dec.haeyum.external.kakao.dto.request.PostKakaoLoginRequestDtoV2;
import dec.haeyum.external.kakao.dto.response.PostKakaoLoginResponseDto;
import dec.haeyum.external.kakao.dto.response.PostKakaoLoginResponseDtoV2;
import dec.haeyum.external.kakao.service.KakaoService;
import dec.haeyum.member.dto.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

//    @Operation(summary = "카카오 로그인", description = "카카오 API로 redirect")
//    @GetMapping("/login")
//    public void loginWithKakao(HttpServletResponse response){
//        kakaoService.KakaoAuthorize(response);
//    }
//    @Operation(summary = "카카오 API 결과 redirect",description = "로그인 된 사용자 정보 수집 후 식별")
//    @GetMapping("/authorize/fallback")
//    public void tokenAccess(@RequestParam("code")String code,
//                            HttpServletResponse response){
//        kakaoService.tokenAccess(code, response);
//    }
//

    @Operation(summary = "카카오 로그인", description = "프론트에서 수집한 kakaoAPI code")
    @PostMapping("/login")
    public ResponseEntity<PostKakaoLoginResponseDto> loginWithKakao(@RequestBody PostKakaoLoginRequestDto dto){
        ResponseEntity<PostKakaoLoginResponseDto> result = kakaoService.login(dto);
        return result;
    }


    @Operation(summary = "카카오 로그인V2", description = "프론트에서 카카오와 통신 후 ID_Token 받고 로그인 처리")
    @PostMapping("/loginV2")
    public ResponseEntity<PostKakaoLoginResponseDtoV2> loginWithKakao(@RequestBody PostKakaoLoginRequestDtoV2 dto){
        ResponseEntity<PostKakaoLoginResponseDtoV2> result = kakaoService.loginV2(dto);
        return result;

    }



    @Operation(summary = "로그아웃", description = "JWT 블랙리스트 && 카카오JWT 만료")
    @GetMapping("/logout")
    public ResponseEntity<PostKakaoLoginResponseDto> logoutWithKakao(HttpServletRequest request){
        kakaoService.logout(request);
        return null;
    }

}
