package dec.haeyum.external.kakao.service;

import dec.haeyum.member.dto.JwtToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface KakaoService {

     void KakaoAuthorize(HttpServletResponse response);

     void tokenAccess(String code, String error, String errorDescription, HttpServletResponse response);

}
