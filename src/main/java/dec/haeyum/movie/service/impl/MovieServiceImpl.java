package dec.haeyum.movie.service.impl;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.movie.dto.MovieInfoDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.entity.MovieEntity;
import dec.haeyum.movie.repository.CalendarMovieRepository;
import dec.haeyum.movie.repository.MovieRepository;
import dec.haeyum.movie.service.MovieService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
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
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final CalendarMovieRepository calendarMovieRepository;
    private final CalendarService calendarService;
    private WebClient webClient;

    @Value("${movie.api-url}")
    private String movie_api_url;

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
                                .maxInMemorySize(10 * 1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public ResponseEntity<GetTop5Movies> getTop5Movies(Long calendarId) {
        // 1. DB 데이터 체크
        List<Top5MoviesDto> top5MoviesDto = calendarMovieRepository.getTop5Movie(calendarId)
                .orElse(null);
        // 2. 없으면 크롤링
        if (top5MoviesDto == null || top5MoviesDto.isEmpty()){
            Optional<CalendarEntity> isCalendar = calendarService.getCalendar(calendarId);
            if (!isCalendar.isPresent()){
                // 에러
            }
            CalendarEntity calendar = isCalendar.get();
            searchMovie(calendar.getCalendarDate());
        }
        // 3. 있으면 해당 데이터 반환
        return GetTop5Movies.success(top5MoviesDto);
    }

    private void searchMovie(LocalDate calendarDate) {

        try {
            String html = getMovieInfoWebClient(calendarDate);
            htmlDataParsing(html);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getMovieInfoWebClient(LocalDate calendarDate) {
        // calendarDate 의 7일 이전
        String sSearchFrom = String.valueOf(calendarDate.minusDays(2));
        String sSearchTo = String.valueOf(calendarDate);
        String CSRFToken = "xj6vtA4p6rTaiq5vAGm1p0p1WhIMJ1K0_IwR4B3N3Bk";


        String html = webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("CSRFToken",CSRFToken)
                        .with("loadEnd","0")
                        .with("searchType","search")
                        .with("sSearchFrom",sSearchFrom)
                        .with("sSearchTo",sSearchTo))
                .retrieve() // 응답 처리
                .bodyToMono(String.class)
                .block();
        return html;
    }

    private void htmlDataParsing(String html) {
        // 영화 페이지
        Document document = Jsoup.parse(html);
        // 날짜 데이터 매핑
        HashMap<String, List<MovieInfoDto>> movieDate = mapMovieDate(html, document);
        // 날짜별 영화 데이터 매핑
        List<MovieInfoDto> movieInfo = mapMovieInfo(document);
        // 영화 디테일 데이터 매핑
        mapMovieDetailInfo(movieInfo);


    }

    private void mapMovieDetailInfo(List<MovieInfoDto> movieInfo) {

    }


    private HashMap<String,List<MovieInfoDto>> mapMovieDate(String html, Document document) {
        HashMap<String,List<MovieInfoDto>> list = new HashMap<>();
        // 날짜 수집
        for (Element element : document.select("div.board_tit")) {
            String movieDate = element.select("h4").html();
            // 날짜별 영화 info 수집
            list.put(movieDate,null);
        }
        return list;
    }

    // 문제가 2틀의 영화 데이터만 요청했는데 마지막에 more 해서 html이 하나의 테이블을 더 건내주는데 거기에 10위 이상의 영화 데이터가 있음;;;
    private List<MovieInfoDto> mapMovieInfo(Document document) {
        List<MovieInfoDto> item = new ArrayList<>();
        for (Element tbody : document.select("table.th_sort tbody tr")) {
            MovieInfoDto dto = new MovieInfoDto();
            mapMovieId(dto, tbody);
            mapMovieLanking(dto, tbody);
            mapMovieTitle(dto, tbody);
            mapMovieOpenDate(dto, tbody);
            item.add(dto);
        }
        return item;
    }

    private void mapMovieId(MovieInfoDto dto, Element tr) {
        String onClick = tr.select("td.tal span a").attr("onClick");
        String movieId = onClick.replaceAll(".*mstView\\('movie','(\\d+)'\\).*", "$1");
        dto.setMovieId(Integer.parseInt(movieId));

    }

    private void mapMovieLanking(MovieInfoDto dto, Element tr) {

        String ranking = tr.select("td[title]").get(0).text();
        dto.setRanking(Integer.parseInt(ranking));
    }
    private void mapMovieOpenDate(MovieInfoDto dto, Element tr) {

        String openDate = tr.select("td").get(2).text();
        LocalDate parse = LocalDate.parse(openDate);
        dto.setOpenDate(parse);

    }

    private void mapMovieTitle(MovieInfoDto dto, Element tr) {

        String title = tr.select("td.tal span a").text();
        dto.setTitle(title);
    }




}
