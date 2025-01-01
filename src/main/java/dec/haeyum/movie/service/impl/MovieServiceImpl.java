package dec.haeyum.movie.service.impl;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.movie.dto.MovieInfoDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.entity.CalendarMovieEntity;
import dec.haeyum.movie.entity.MovieEntity;
import dec.haeyum.movie.repository.CalendarMovieRepository;
import dec.haeyum.movie.repository.MovieRepository;
import dec.haeyum.movie.service.MovieService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final CalendarMovieRepository calendarMovieRepository;
    private final CalendarService calendarService;
    private WebClient webClient;

    @Value("${movie.base-url}")
    private String movie_api_url;
    @Value("${movie.rank-url}")
    private String movie_rank_url;
    @Value("${movie.detail-url}")
    private String movie_detail_url;
    @Value("${spring.file.filePath}")
    private String file_path;

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
    @Transactional
    public ResponseEntity<GetTop5Movies> getTop5Movies(Long calendarId) {
        List<Top5MoviesDto> top5MoviesDto = new ArrayList<>();
        // 1. DB 데이터 체크
        top5MoviesDto = calendarMovieRepository.getTop5Movie(calendarId)
                .orElse(null);
        // 2. 없으면 스크랩핑
        if (top5MoviesDto == null || top5MoviesDto.isEmpty()){
            CalendarEntity calendar = calendarService.getCalendar(calendarId)
                    .orElse(null);
            if (calendar == null){
                throw new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR);
            }
            searchMovie(calendar);
            top5MoviesDto = calendarMovieRepository.getTop5Movie(calendarId)
                    .get();
        }

        // 3. 있으면 해당 데이터 반환
        return GetTop5Movies.success(top5MoviesDto);
    }

    private void searchMovie(CalendarEntity calendar) {
            // 페이지 Get
            String html = getMovieInfoWebClient(calendar.getCalendarDate());
            if (html == null || html.isEmpty()){
                throw new BusinessException(ErrorCode.NOT_CONNECT_PAGE);
            }
            // 페이지에서 원하는 데이터 추출
            movieInfoParsing(html, calendar);
    }


    private void movieInfoParsing(String html, CalendarEntity calendar) {
        // 영화 페이지
        Document document = Jsoup.parse(html);
        // 날짜 데이터 매핑
        HashMap<String, List<MovieInfoDto>> movieDate = mapMovieDate(html, document);
        // 날짜별 영화 데이터 매핑(영화별 영화ID, 순위, 제목, 개봉일)
        List<MovieInfoDto> movieInfo = mapMovieInfo(document);
        // 중복 영화 제거
        Set<MovieInfoDto> infoDtos = changeDataStructure(movieInfo);
        // movieUuid 만 추출해서 있는거
        Set<Integer> collect = infoDtos.stream()
                .map(item -> item.getMovieUuid())
                .collect(Collectors.toSet());
        // DB에 있는 uuid  검증 , 존재하면 uuid 반환
        Set<Integer> byMovieUuid = movieRepository.findByMovieUuid(collect);
        // DB uuid - 중복 제거 List = DB에 저장되지 않은 영화
        List<MovieInfoDto> list = infoDtos.stream()
                .filter(movie -> !byMovieUuid.contains(movie.getMovieUuid()))
                .toList();
        if (list.size() >= 1){
            // 영화 디테일 데이터 매핑(영화별 이미지, 감독)
            getMovieDetailInfoWebClient(list, calendar);
        }
    }



    private String getMovieInfoWebClient(LocalDate calendarDate) {
        //String sSearchFrom = String.valueOf(calendarDate.minusDays(3));
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

    private Set<MovieInfoDto> changeDataStructure(List<MovieInfoDto> movieInfo) {

        Set<MovieInfoDto> list = new HashSet<>();
        list.addAll(movieInfo);

        return list;
    }

    private void getMovieDetailInfoWebClient(List<MovieInfoDto> list, CalendarEntity calendar) {

        final String csrfToken = "yoH3nEsLHvex4kzCaKSNdH7pAbtthxALcxPWK03l5OQ";

        for (MovieInfoDto data : list) {
            String html = webClient.post()
                    .uri(movie_detail_url)
                    .body(BodyInserters.fromFormData(
                                    "code", String.valueOf(data.getMovieUuid()))
                            .with("titleYN", "Y")
                            .with("isOuterReq", "false")
                            .with("CSRFToken", csrfToken))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            movieDetailInfoParsing(html,data.getMovieUuid(),data);
            MovieEntity movie = movieRepository.save(new MovieEntity(data));
            calendarMovieRepository.save(new CalendarMovieEntity(calendar,movie,data.getRanking()));


        }
    }

    private void movieDetailInfoParsing(String result, Integer movieUuid, MovieInfoDto data) {
        Document document = Jsoup.parse(result);
        String imgPath = document.select("a.fl").attr("href");
        String directorPath = String.format("div#%d_director dd a", movieUuid);

        String directorName = document.select(directorPath).text();
        String content = document.select("p.desc_info").text();

        byte[] imgFile = downloadImage(imgPath);
        String fileName = saveImgFile(imgFile);

        data.setContent(content);
        data.setImg(fileName);
        data.setDirector(directorName);

    }

    private String saveImgFile(byte[] imgFile) {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + ".jpg";
        String pullPath = file_path + fileName;
        Path path = Paths.get(pullPath);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path,imgFile);
        }catch (IOException e){
            throw new BusinessException(ErrorCode.NOT_CONNECT_PAGE);
        }
        return fileName;
    }

    private byte[] downloadImage(String imgPath) {
        final String pullPath = movie_api_url + imgPath;
        return webClient.get()
                .uri(pullPath)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
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
        dto.setMovieUuid(Integer.parseInt(movieId));

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
