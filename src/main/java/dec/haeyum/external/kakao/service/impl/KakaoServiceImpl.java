package dec.haeyum.external.kakao.service.impl;

import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.external.kakao.dto.KakaoTokenInfo;
import dec.haeyum.external.kakao.dto.KakaoTokenInfoError;
import dec.haeyum.external.kakao.dto.request.PostKakaoLoginRequestDto;
import dec.haeyum.external.kakao.dto.request.PostKakaoLoginRequestDtoV2;
import dec.haeyum.external.kakao.dto.response.KakaoLogoutResponseDto;
import dec.haeyum.external.kakao.dto.response.PostKakaoLoginResponseDto;
import dec.haeyum.external.kakao.dto.response.PostKakaoLoginResponseDtoV2;
import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.external.kakao.service.KakaoService;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.redis.RedisService;
import dec.haeyum.social.service.SocialService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    @Value("${kaKao.kakaoAuthorizeUrl}")
    private String kakaoAuthorizeUrl;
    @Value("${kaKao.kakaoAuthorizeRedirectUrl}")
    private String kakaoAuthorizeRedirectUrl;
    @Value("${kaKao.kakaoTokenUrl}")
    private String kakaoTokenUrl;
    @Value("${kaKao.kakaoOIDCInfo}")
    private String kakaoOIDCInfo;
    @Value("${kaKao.kakaoLogoutUrl}")
    private String kakaoLogout;
    private WebClient webClient;
    @Value("${kaKao.clientId}")
    private String clientId;
    @Value("${kaKao.adminKey}")
    private String adminKey;
    @Value("${kaKao.nonce}")
    private String nonce;
    @Value("${kaKao.sendRedirect}")
    private String sendRedirect;

    private final SocialService socialService;
    private final RedisService redisService;


    @PostConstruct
    public void init(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(30 * 1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public void KakaoAuthorize(HttpServletResponse response) {
        try {

            String redirectUrl = kakaoAuthorizeUrl
                    + "?client_id=" + clientId
                    + "&redirect_uri=" + kakaoAuthorizeRedirectUrl
                    + "&response_type=" + "code"
                    + "&nonce=" + nonce;
            response.sendRedirect(redirectUrl);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void tokenAccess(String code, HttpServletResponse servletResponse) {
        log.info("start");

        try {

            TokenAccessResponseDto response = webClient.post()
                    .uri(kakaoTokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", clientId)
                            .with("redirect_uri", kakaoAuthorizeRedirectUrl)
                            .with("code", code))
                    .retrieve()
                    .bodyToMono(TokenAccessResponseDto.class)
                    .block();
            JwtToken jwtToken = socialService.validateMember(response);
            log.info("JwtToken ={}",jwtToken);
            // 레디스에 social_sub 별 refreshToken 저장
            redisService.setRefreshTokenInString(response.idTokenDecode().getSub(), jwtToken.getRefreshToken());
            servletResponse.sendRedirect(sendRedirect + jwtToken.getAccessToken() + "&refreshToken=" + jwtToken.getRefreshToken() + "&socialSub=" + response.idTokenDecode().getSub());


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // 1. JWT 블랙리스트 등록
        String sub = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 카카오 토큰 만료
        KakaoLogoutResponseDto response = webClient.post()
                .uri(kakaoLogout)
                .header("Authorization", "KakaoAk " + adminKey)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("target_id_type", "user_id")
                        .with("target_id", sub))
                .retrieve()
                .bodyToMono(KakaoLogoutResponseDto.class)
                .block();

        socialService.logout(response, request);

        return null;

    }

    @Override
    public ResponseEntity<PostKakaoLoginResponseDto> login(PostKakaoLoginRequestDto dto) {
        // 반환 1. accessToken, 2. refreshToken, 3. social_id
        try {
            TokenAccessResponseDto response = webClient.post()
                    .uri(kakaoTokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", clientId)
                            .with("redirect_uri", kakaoAuthorizeRedirectUrl)
                            .with("code", dto.getCode()))
                    .retrieve()
                    .bodyToMono(TokenAccessResponseDto.class)
                    .block();


            log.info(" = {}", response.toString());
            JwtToken jwtToken = socialService.validateMember(response);
            log.info("JwtToken ={}", jwtToken);
            // 레디스에 social_sub 별 refreshToken 저장
            redisService.setRefreshTokenInString(response.idTokenDecode().getSub(), jwtToken.getRefreshToken());
            return PostKakaoLoginResponseDto.success(jwtToken, response.idTokenDecode().getSub());
        } catch (Exception e) {
            // DB 에러
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<PostKakaoLoginResponseDtoV2> loginV2(PostKakaoLoginRequestDtoV2 dto) {

        // 1. 엑세스토큰 과 id토큰 받아서 ocid 검증하고 userid 반환
        KakaoTokenInfo kakaoInfo = webClient.post()
                .uri(kakaoOIDCInfo)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("id_token", dto.getIdToken()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() ,
                        clientResponse -> clientResponse.bodyToMono(KakaoTokenInfoError.class).flatMap(error -> Mono.error(new BusinessException(ErrorCode.EXPIRED_TOKNE))))
                .bodyToMono(KakaoTokenInfo.class)
                .block();


        Boolean isValidate = validateKakaoToken(kakaoInfo);

        if (!isValidate){
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        JwtToken jwtToken = socialService.validateMemberV2(kakaoInfo);

        return PostKakaoLoginResponseDtoV2.succeess(jwtToken, kakaoInfo.getSub());


    }

    private Boolean validateKakaoToken(KakaoTokenInfo kakaoInfo) {

        // aud 검증
        if (clientId != kakaoInfo.getAud()){
            return false;
        }
        return true;

    }


}
