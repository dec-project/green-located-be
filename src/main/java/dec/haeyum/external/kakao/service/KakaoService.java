package dec.haeyum.external.kakao.service;

import dec.haeyum.member.dto.JwtToken;
import org.springframework.http.ResponseEntity;

public interface KakaoService {

     ResponseEntity<Void> KakaoAuthorize();

     ResponseEntity<JwtToken> tokenAccess(String code, String error, String errorDescription);

}
