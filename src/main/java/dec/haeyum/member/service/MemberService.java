package dec.haeyum.member.service;

import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.member.dto.*;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    JwtToken signIn(String memberId, String password);
    void signUp(SignUpDto signUpDto);
    void signOut(String accessToken);


    ResponseEntity<GetSearchProfileResponseDto> searchProfile();

    void updateProfile(PostUpdateProfileRequestDto dto);

    ResponseEntity<GetSearchFavoriteResponseDto> searchFavorite(Long calendarId);

    ResponseEntity<PutUpdateFavoriteResponseDto> updateFavorite(Long calendarId);

    ResponseEntity<GetFavoriteListResponseDto> favoriteList();
}
