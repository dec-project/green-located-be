package dec.haeyum.movie.service.impl;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.external.youtube.service.YoutubeService;
import dec.haeyum.movie.dto.CrowlingMovieData;
import dec.haeyum.movie.dto.response.GetMovieDetailResponseDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.dto.response.MovieDbKeyDto;
import dec.haeyum.movie.entity.CalendarMovieEntity;
import dec.haeyum.movie.entity.MovieEntity;
import dec.haeyum.movie.repository.CalendarMovieRepository;
import dec.haeyum.movie.repository.MovieRepository;
import dec.haeyum.movie.service.MovieService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.View;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImplV2 implements MovieService {

    @Value("${spring.file.fileUrl}")
    private String fileUrl;
    @Value("${movie.base-url}")
    private String movie_api_url;
    @Value("${movie.rank-url}")
    private String movie_rank_url;
    @Value("${movie.detail-url}")
    private String movie_detail_url;
    @Value("${spring.file.filePath}")
    private String file_path;
    @Value("${spring.config.activate.on-profile}")
    private String profile;


    private final CalendarMovieRepository calendarMovieRepository;
    private final MovieRepository movieRepository;
    private final CalendarService calendarService;
    private WebClient webClient;



    @PostConstruct
    public void initWebClient(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(movie_api_url);;
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(movie_api_url)
                .defaultHeader(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(30 * 1024 * 1024))
                        .build())
                .build();
    }



    @Override
    public ResponseEntity<GetTop5Movies> getTop5Movies(Long calendarId) {

        Optional<List<Top5MoviesDto>> top5Movie = calendarMovieRepository.getTop5Movie(calendarId);
        if (top5Movie == null || top5Movie.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_MOVIE);
        }
        List<Top5MoviesDto> top5MoviesDtos = top5Movie.get();

        return GetTop5Movies.success(top5MoviesDtos,fileUrl);
    }

    @Override
    public ResponseEntity<GetMovieDetailResponseDto> getMovieDetail(Long calendarId, Long movieId) {

        Optional<MovieEntity> byId = movieRepository.findById(movieId);
        if (byId == null || byId.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_MOVIE);
        }
        MovieEntity movie = byId.get();
        return GetMovieDetailResponseDto.success(movie);

    }


    protected String crowlingMovie( String inputEndDate){
        LocalDate startDate = LocalDate.of(2023,11,11);
        LocalDate endDate = LocalDate.parse(inputEndDate);
        // DB의 마지막 크롤링 날짜 확인 후 크롤링
        Optional<CalendarMovieEntity> byLastCalendar = calendarMovieRepository.findByLastCalendar();

        if (byLastCalendar.isPresent()){
            CalendarMovieEntity calendarMovieEntity = byLastCalendar.get();
            CalendarEntity calendar = calendarService.getCalendar(calendarMovieEntity.getCalendarId());
            LocalDate localDate = calendar.getCalendarDate().plusDays(1);
            startDate = localDate;
        }

        while (!startDate.isEqual(endDate)){
            int check = 0;
            String html = getMovieInfoWebClient(startDate);
            if (html == null || html.isEmpty()){
                log.info("{} 일 에는 영화 데이터가 없습니다.",startDate);
                startDate.plusDays(1);
                continue;
            }

        }

        // 2. list 사이즈 만큼 크롤링 시작
        // 3. 하나의 list에 5위까지의 데이터 수집 하고 각 데이터 마다 디테일 데이터 수집

        return null;
    }


    private String getMovieInfoWebClient(LocalDate calendarDate) {

        String sSearchFrom = String.valueOf(calendarDate);
        String sSearchTo = String.valueOf(calendarDate);
        String CSRFToken = "xj6vtA4p6rTaiq5vAGm1p0p1WhIMJ1K0_IwR4B3N3Bk";

        String html = webClient.post()
                .uri(movie_rank_url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                                "CSRFToken",CSRFToken)
                        .with("loadEnd","0")
                        .with("searchType","search")
                        .with("sSearchFrom",sSearchFrom)
                        .with("sSearchTo",sSearchTo))
                .retrieve() // 응답 처리
                .bodyToMono(String.class)
                .block();
        return html;
    }


}
