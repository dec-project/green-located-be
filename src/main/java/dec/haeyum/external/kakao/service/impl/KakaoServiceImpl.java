package dec.haeyum.external.kakao.service.impl;

import dec.haeyum.external.kakao.dto.response.TokenAccessResponseDto;
import dec.haeyum.external.kakao.service.KakaoService;
import dec.haeyum.member.dto.JwtToken;
import dec.haeyum.social.service.SocialService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

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
    private WebClient webClient;
    @Value("${kaKao.clientId}")
    private String clientId;
    @Value("${kaKao.nonce}")
    private String nonce;

    private final SocialService socialService;


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
    public ResponseEntity<Void> KakaoAuthorize() {
        String redirectUrl = kakaoAuthorizeUrl
                + "?client_id=" + clientId
                + "&redirect_uri=" + kakaoAuthorizeRedirectUrl
                + "&response_type=" + "code"
                + "&nonce=" + nonce;

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION,redirectUrl)
                .build();

    }

    @Override
    public ResponseEntity<JwtToken> tokenAccess(String code, String error, String errorDescription) {


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
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);

    }



}
