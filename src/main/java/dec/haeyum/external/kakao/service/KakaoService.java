package dec.haeyum.external.kakao.service;

import dec.haeyum.external.kakao.dto.request.PostKakaoLoginRequestDto;
import dec.haeyum.external.kakao.dto.response.PostKakaoLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface KakaoService {

     void KakaoAuthorize(HttpServletResponse response);

     void tokenAccess(String code, HttpServletResponse response);

    ResponseEntity<Void> logout(HttpServletRequest request);

    ResponseEntity<PostKakaoLoginResponseDto> login(PostKakaoLoginRequestDto code);
}
