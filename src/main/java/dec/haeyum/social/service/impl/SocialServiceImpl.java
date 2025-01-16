package dec.haeyum.social.service.impl;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.img.service.ImgService;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.member.jwt.JwtTokenProvider;
import dec.haeyum.social.entity.SocialEntity;
import dec.haeyum.social.repository.SocialRepository;
import dec.haeyum.social.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialRepository socialRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImgService imgService;


    @Override
    @Transactional
    public JwtToken validateMember(TokenAccessResponseDto response) {
        // sub를 사용해서 회원 구분
        Boolean isMember = findMember(response);
        // 신규 유저
        if (!isMember){
            createMember(response);
        }
        JwtToken jwtToken = jwtTokenProvider.generateTokenWithKakao(response.idTokenDecode().getSub());
        return jwtToken;
    }

    private void createMember(TokenAccessResponseDto response){
        SocialEntity socialEntity = new SocialEntity();
        String imgName = imgService.downloadImg(response.idTokenDecode().getPicture());
        socialEntity.createSocialWithKakao(response,imgName);
        socialRepository.save(socialEntity);
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
