package dec.haeyum.member.service;

import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.dto.SignUpDto;

public interface MemberService {
    JwtToken signIn(String memberId, String password);
    void signUp(SignUpDto signUpDto);
    void signOut(String accessToken);


}
