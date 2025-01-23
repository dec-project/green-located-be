package dec.haeyum.social.service;

import dec.haeyum.external.kakao.dto.response.KakaoLogoutResponseDto;
import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface SocialService {
    JwtToken validateMember(TokenAccessResponseDto response);

    Member findMember(String sub);

    void logout(KakaoLogoutResponseDto response, HttpServletRequest request);
}
