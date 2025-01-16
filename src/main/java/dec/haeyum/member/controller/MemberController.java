package dec.haeyum.member.controller;

import dec.haeyum.member.dto.GetSearchProfileResponseDto;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.dto.SignInDto;
import dec.haeyum.member.dto.SignUpDto;
import dec.haeyum.member.jwt.JwtTokenProvider;
import dec.haeyum.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(@RequestBody SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(username, password);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseEntity.ok("가입이 완료되었습니다.");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String > signOut(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.signOut(accessToken);

        return ResponseEntity.ok("signed out successfully.");
    }

    @GetMapping("/member/profile")
    public ResponseEntity<GetSearchProfileResponseDto> searchProfile(@AuthenticationPrincipal String accessToken){
        memberService.searchProfile(accessToken);
        return null;
    }



    @GetMapping("/jwt-test")
    public String test() {
        return "success!";}
}
