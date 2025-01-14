package dec.haeyum.social.service;

import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.member.dto.JwtToken;

import java.io.IOException;

public interface SocialService {
    JwtToken validateMember(TokenAccessResponseDto response);
}
