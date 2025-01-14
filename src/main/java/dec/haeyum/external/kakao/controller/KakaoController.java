package dec.haeyum.external.kakao.controller;

import dec.haeyum.external.kakao.service.KakaoService;
import dec.haeyum.member.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public ResponseEntity<Void> loginWithKakao(){
        ResponseEntity<Void> result = kakaoService.KakaoAuthorize();
        return result;
    }

    @GetMapping("/authorize/fallback")
    public ResponseEntity<JwtToken> tokenAccess(@RequestParam("code")String code, @RequestParam(value = "error", required = false)String error,
                                                @RequestParam(value = "error_description", required = false)String errorDescription){

        ResponseEntity<JwtToken> result = kakaoService.tokenAccess(code, error, errorDescription);
        return result;
    }

}
