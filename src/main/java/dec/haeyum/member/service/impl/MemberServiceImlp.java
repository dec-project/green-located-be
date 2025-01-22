package dec.haeyum.member.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.img.service.ImgService;
import dec.haeyum.member.dto.*;
import dec.haeyum.member.entity.Member;
import dec.haeyum.member.jwt.JwtTokenProvider;
import dec.haeyum.member.reopository.MemberRepository;
import dec.haeyum.member.service.MemberService;
import dec.haeyum.redis.RedisService;
import dec.haeyum.social.service.SocialService;
import dec.haeyum.song.service.SongService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImlp implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;
    private final SocialService socialService;
    private final ImgService imgService;
    private final CalendarService calendarService;
    private final SongService songService;

    @Value("${spring.file.fileUrl}")
    private String fileUrl;

    @Override
    @Transactional
    public JwtToken signIn(String username, String password) {

        // 1. username + password 기반으로 Authentication 객체 생성
        // 이 때 Authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate()를 통해 요청된 Member에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 jwt token 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. redis에 refresh token 저장
        long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenExpirationMillis();
        redisService.setValues(authentication.getName(), jwtToken.getRefreshToken(), Duration.ofMillis(refreshTokenExpirationMillis));
        log.info("username? : {}", authentication.getName());

        return jwtToken;
    }

    @Override
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        validateDuplicateMember(signUpDto.getUsername());

        //password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  //USER 권한 부여
        memberRepository.save(signUpDto.toEntity(encodedPassword, roles));
    }

    private void validateDuplicateMember(String username) {
        Optional<Member> findMembers = memberRepository.findByUsername(username);
        if(findMembers.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USERNAME);
        }
    }

    @Override
    public void signOut(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();

        if (!redisService.getValues(username).equals("false")) {
            redisService.deleteValues(username);

            // 로그아웃시 Access Token 블랙리스트에 저장
            long accessTokenExpirationMillis = jwtTokenProvider.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
            log.info("logout success");
        }
    }
    @Override
    public ResponseEntity<GetSearchProfileResponseDto> searchProfile() {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("context ={}",context);
        Member member = socialService.findMember(sub);
        return GetSearchProfileResponseDto.success(fileUrl,member);
    }

    @Override
    @Transactional
    public void updateProfile(PostUpdateProfileRequestDto dto) {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = socialService.findMember(sub);

        if (dto.getProfileImg() != null){
            String img = imgService.downloadImg(dto.getProfileImg());
            member.setProfileImg(img);
        }
        if (dto.getNickname() != null && !"".equals(dto.getNickname())){
            if (dto.getNickname().length() < 2 || dto.getNickname().length() > 10){
                throw new BusinessException(ErrorCode.NOT_EXISTED_LENGTH);
            }
            member.setUsername(dto.getNickname());
        }

    }

    @Override
    public ResponseEntity<GetSearchFavoriteResponseDto> searchFavorite(Long calendarId) {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = socialService.findMember(sub);
        Optional<CalendarEntity> isFavorite = member.getFavorite().stream()
                .filter(target -> target.getCalendarId().equals(calendarId))
                .findFirst();
        if (isFavorite.isPresent()){
            return GetSearchFavoriteResponseDto.success(true);
        }
        return GetSearchFavoriteResponseDto.success(false);
    }

    @Override
    @Transactional
    public ResponseEntity<PutUpdateFavoriteResponseDto> updateFavorite(Long calendarId) {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = socialService.findMember(sub);
        CalendarEntity calendar = calendarService.getCalendar(calendarId);

        Optional<CalendarEntity> isFavorite = member.getFavorite().stream()
                .filter(target -> target.getCalendarId().equals(calendarId))
                .findFirst();

        if (isFavorite == null || isFavorite.isEmpty()){
            member.getFavorite().add(calendar);
            return PutUpdateFavoriteResponseDto.success(true);
        }
        if (isFavorite.isPresent()){
            member.getFavorite().remove(calendar);
        }
        return PutUpdateFavoriteResponseDto.success(false);
    }

    @Override
    public ResponseEntity<GetFavoriteListResponseDto> favoriteList() {
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = socialService.findMember(sub);
        List<CalendarEntity> favorite = member.getFavorite();
        List<FavoriteItem> itemList = member.getFavorite().stream()
                .map(calendar -> {
                    String calendarSongImageUrl = songService.getCalendarSongImageUrl(calendar.getCalendarId());
                    return new FavoriteItem(
                            calendar.getCalendarId(),
                            calendar.getCalendarName(),
                            calendarSongImageUrl
                    );
                })
                .collect(Collectors.toList());

        return GetFavoriteListResponseDto.success(itemList);
    }


}
