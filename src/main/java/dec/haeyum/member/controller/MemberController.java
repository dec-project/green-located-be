package dec.haeyum.member.controller;

import dec.haeyum.member.dto.*;
import dec.haeyum.member.jwt.JwtTokenProvider;
import dec.haeyum.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    @Operation(summary = "프로필 조회", description = "JWT 토큰 으로 유저 식별 후 이미지, 닉네임 반환")
    @GetMapping("/member/profile")
    public ResponseEntity<GetSearchProfileResponseDto> searchProfile(){
        ResponseEntity<GetSearchProfileResponseDto> result = memberService.searchProfile();
        return result;
    }
    @Operation(summary = "프로필 수정", description = "JWT 토큰 으로 유저 식별 후 이미지, 닉네임 수정")
    @PutMapping("/member/profile")
    public ResponseEntity<Void> updateProfile(@ModelAttribute PostUpdateProfileRequestDto dto){
        memberService.updateProfile(dto);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "찜 조회",description = "JWT 토큰 으로 유저 식별 후 캘린더 좋아요 여부 조회")
    @GetMapping("/favorite/{calendarId}")
    public ResponseEntity<GetSearchFavoriteResponseDto> searchFavorite(@PathVariable("calendarId") Long calendarId){
        ResponseEntity<GetSearchFavoriteResponseDto> result = memberService.searchFavorite(calendarId);
        return result;
    }
    @Operation(summary = "찜 수정", description = "JWT 토큰 으로 유저 식별 후 캘린더 좋아요 수정")
    @PutMapping("/favorite/{calendarId}")
    public ResponseEntity<PutUpdateFavoriteResponseDto> updateFavorite(@PathVariable("calendarId") Long calendarId){
        ResponseEntity<PutUpdateFavoriteResponseDto> result = memberService.updateFavorite(calendarId);
        return result;

    }
    @Operation(summary = "사용자의 좋아요 목록 반환", description = "JWT 토큰 으로 유저 식별 후 좋아요 목록 반환")
    @GetMapping("/member/favorite")
    public ResponseEntity<GetFavoriteListResponseDto> favoriteList(){
        ResponseEntity<GetFavoriteListResponseDto> result = memberService.favoriteList();
        return result;
    }

    @GetMapping("/jwt-test")
    public String test() {
        return "success!";}
}
