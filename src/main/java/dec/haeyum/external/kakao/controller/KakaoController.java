package dec.haeyum.external.kakao.controller;

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

    @Operation(summary = "카카오 로그인", description = "카카오 API로 redirect")
    @GetMapping("/login")
    public void loginWithKakao(HttpServletResponse response){
        kakaoService.KakaoAuthorize(response);
    }
    @Operation(summary = "카카오 API 결과 redirect",description = "로그인 된 사용자 정보 수집 후 식별")
    @GetMapping("/authorize/fallback")
    public void tokenAccess(@RequestParam("code")String code, @RequestParam(value = "error", required = false)String error,
                                                @RequestParam(value = "error_description", required = false)String errorDescription, HttpServletResponse response){
        kakaoService.tokenAccess(code, error, errorDescription, response);
    }

    @Operation(summary = "로그아웃", description = "JWT 블랙리스트 && 카카오JWT 만료")
    @GetMapping("/logout")
    public ResponseEntity<Void> logoutWithKakao(HttpServletRequest request){
        kakaoService.logout(request);
        return null;
    }

}
