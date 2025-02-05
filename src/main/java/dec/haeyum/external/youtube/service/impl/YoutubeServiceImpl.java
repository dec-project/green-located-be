package dec.haeyum.external.youtube.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dec.haeyum.external.youtube.dto.YoutubeDetailDto;
import dec.haeyum.external.youtube.service.YoutubeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.AcceptEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class YoutubeServiceImpl implements YoutubeService {

    private WebClient webClient;
    @Value("${youtube.api-key}")
    private String serviceKey;
    @Value("${youtube.baseUrl}")
    private String youtube_api_url;

    @PostConstruct
    public void initWebClient(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(youtube_api_url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(youtube_api_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING,"gzip")
                .build();
    }

    @Override
    public YoutubeDetailDto searchVideoUrl(String searchWord){

        return apiCall(searchWord);


    }

    private YoutubeDetailDto apiCall(String searchWord) {

        YoutubeDetailDto youtubeDetailDto = new YoutubeDetailDto();

        try {

            String encodeWord = URLEncoder.encode(searchWord, StandardCharsets.UTF_8);
            String block = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("key", serviceKey)
                            .queryParam("q", encodeWord)
                            .queryParam("part", "snippet")
                            .queryParam("type", "video")
                            .queryParam("maxResults", 1)
                            .queryParam("videoEmbeddable", true)
                            .queryParam("fields", "items(id,snippet(title,publishedAt))")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(block);
            JsonNode itemList = node.path("items");

            if (itemList.isArray() && itemList.size() > 0){
                for (JsonNode jsonNode : itemList) {
                    String videoId = jsonNode.path("id").path("videoId").asText();
                    String releaseDate = jsonNode.path("snippet").path("publishedAt").asText();
                    youtubeDetailDto.setData(videoId,releaseDate);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return youtubeDetailDto;
    }

}
