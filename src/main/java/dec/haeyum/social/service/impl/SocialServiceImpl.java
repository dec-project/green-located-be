package dec.haeyum.social.service.impl;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.external.kakao.dto.KakaoTokenInfo;
import dec.haeyum.external.kakao.dto.response.KakaoLogoutResponseDto;
import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.img.service.ImgService;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.entity.Member;
import dec.haeyum.member.jwt.JwtTokenProvider;
import dec.haeyum.redis.RedisService;
import dec.haeyum.social.entity.SocialEntity;
import dec.haeyum.social.repository.SocialRepository;
import dec.haeyum.social.service.SocialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialRepository socialRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImgService imgService;
    private final RedisService redisService;


    @Override
    @Transactional
    public JwtToken validateMember(TokenAccessResponseDto response) {
        // sub를 사용해서 회원 구분
        Boolean isMember = findMember(response);
        List<String> roles = new ArrayList<>();
        // 신규 유저
        if (!isMember){
            roles = createMember(response);
        }
        if (isMember){
            roles = searchRoles(response);
        }

        JwtToken jwtToken = jwtTokenProvider.generateTokenWithKakao(response.idTokenDecode().getSub(), roles);
        return jwtToken;

    }

    public Member findMember(String subject){
        return socialRepository.findBySocialSub(subject)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTED_SUB))
                .getMember();
    }


    private Boolean findMemberV2(String sub) {

        if (sub == null || sub.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_SUB);
        }
        Optional<SocialEntity> bySocialSub = socialRepository.findBySocialSub(sub);
        if (bySocialSub.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public void logout(KakaoLogoutResponseDto response, HttpServletRequest request) {
        // 레디스에 refresh 삭제
        redisService.deleteRefreshToken(response.getId());
        // 블랙리스트에 refresh 등록
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        log.info("logout::AccessToken = {}",accessToken);
        redisService.setBlackListOfRefreshToken(accessToken);

    }

    @Override
    public JwtToken validateMemberV2(KakaoTokenInfo kakaoInfo) {
        // sub를 사용해서 회원 구분
        Boolean isMember = findMemberV2(kakaoInfo.getSub());
        List<String> roles = new ArrayList<>();
        // 신규 유저
        if (!isMember){
            roles = createMemberV2(kakaoInfo);
        }
        if (isMember){
            roles = searchRolesV2(kakaoInfo);
        }

        JwtToken jwtToken = jwtTokenProvider.generateTokenWithKakao(kakaoInfo.getSub(), roles);
        return jwtToken;

    }

    private List<String> searchRolesV2(KakaoTokenInfo kakaoInfo) {
        SocialEntity socialEntity = socialRepository.findBySocialSub(kakaoInfo.getSub())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTED_SUB));
        return socialEntity.getMember().getRoles();
    }

    private List<String> createMemberV2(KakaoTokenInfo kakaoInfo) {
        SocialEntity socialEntity = new SocialEntity();
        String imgName = imgService.downloadImg(kakaoInfo.getPicture());
        List<String> roles = socialEntity.createSocialWithKakao(kakaoInfo, imgName);
        socialRepository.save(socialEntity);

        return roles;

    }


    private List<String> searchRoles(TokenAccessResponseDto response) {
        SocialEntity socialEntity = socialRepository.findBySocialSub(response.idTokenDecode().getSub())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTED_SUB));
        return socialEntity.getMember().getRoles();

    }

    private List<String> createMember(TokenAccessResponseDto response){
        SocialEntity socialEntity = new SocialEntity();
        String imgName = imgService.downloadImg(response.idTokenDecode().getPicture());
        List<String> roles = socialEntity.createSocialWithKakao(response, imgName);
        socialRepository.save(socialEntity);

        return roles;

    }

    private Boolean findMember(TokenAccessResponseDto response) {
        String sub = response.idTokenDecode().getSub();
        if (sub == null || sub.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_SUB);
        }
        Optional<SocialEntity> bySocialName = socialRepository.findBySocialSub(sub);
        if (bySocialName.isPresent()){
            return true;
        }
        return false;
    }


}
