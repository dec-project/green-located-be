package dec.haeyum.movie.service.impl;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.external.youtube.dto.YoutubeDetailDto;
import dec.haeyum.external.youtube.service.YoutubeService;
import dec.haeyum.movie.dto.CalendarMovieItem;
import dec.haeyum.movie.dto.MovieInfoDto;
import dec.haeyum.movie.dto.response.GetMovieDetailResponseDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.dto.response.MovieDbKeyDto;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private final YoutubeService youtubeService;
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

        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_CONNECT_PAGE));

        if (movie.getYoutube_address() == null || movie.getYoutube_address().isEmpty()){
            String word = "영화 " + movie.getTitle() + " 예고편";
            YoutubeDetailDto youtubeDetailDto = youtubeService.searchVideoUrl(word);
            movie.setYoutubeData(youtubeDetailDto);
        }
        return GetMovieDetailResponseDto.success(movie);

    }


    @Transactional
    public void crawlingMovie(String inputEndDate){
        LocalDate startDate = LocalDate.of(2003,11,11);
        LocalDate endDate = LocalDate.parse(inputEndDate);
        final String csrfToken = "yoH3nEsLHvex4kzCaKSNdH7pAbtthxALcxPWK03l5OQ";
        // DB의 마지막 크롤링 날짜 확인 후 크롤링
        Optional<CalendarMovieEntity> byLastCalendar = calendarMovieRepository.findByLastCalendar();

        Long calendarId = 0L;
        if (byLastCalendar.isPresent()){
            CalendarMovieEntity calendarMovieEntity = byLastCalendar.get();
            CalendarEntity calendar = calendarService.getCalendar(calendarMovieEntity.getCalendarId());
            startDate = calendar.getCalendarDate().plusDays(1);
            calendarId = calendar.getCalendarId();
        }else {
            calendarId = calendarService.getCalendar(startDate).getCalendarId();
        }

        while (!startDate.isEqual(endDate)){
            log.error("startDate ={}",startDate);
            ArrayList<MovieInfoDto> movieEntities = new ArrayList<>();
            ArrayList<CalendarMovieItem> calendarMovieEntities = new ArrayList<>();
            String html = getMovieInfoWebClient(startDate);
            if (html == null || html.isEmpty()){
                log.error("{} 일 에는 영화 데이터가 없습니다.",startDate);
                startDate = startDate.plusDays(1);
                continue;
            }

            // 일단 5위까지 수집하고 DB하고 체크해서 movieUUID 중복되는거 제거 하고 수집

            parseMovieRanking(html, movieEntities, calendarMovieEntities,calendarId);

            Set<String> collect = movieEntities.stream()
                    .map(item -> item.getMovieUuid())
                    .collect(Collectors.toSet());

            List<MovieDbKeyDto> existingMovies = movieRepository.findByMovieUuid(collect);
            // movieEntites - DB = 저장하지 않은 데이터
            List<MovieInfoDto> list = movieEntities.stream()
                    .filter(item -> existingMovies.stream()
                            .noneMatch(target -> Objects.equals(item.getMovieUuid(), target.getMovieUuid())))
                    .toList();

            // DB에 저장되어 있는 데이터
            for (CalendarMovieItem calendarMovieItem : calendarMovieEntities) {
                Optional<MovieDbKeyDto> existingMovie = existingMovies.stream()
                        .filter(item -> item.getMovieUuid().equals(calendarMovieItem.getMovieUuid()))
                        .findFirst();
                if (existingMovie.isPresent()) {
                    CalendarMovieEntity calendarMovieEntity = new CalendarMovieEntity(calendarId, existingMovie.get().getMovieId(), calendarMovieItem.getRanking());
                    calendarMovieRepository.save(calendarMovieEntity);
                }
            }

            // DB에 저장되지 않은 데이터
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            List<CompletableFuture<Void>> futures = list.stream()
                    .map(data -> CompletableFuture.runAsync(() -> {
                        try {
                            String detailPage = webClient.post()
                                    .uri(movie_detail_url)
                                    .header("Accept-Encoding", "gzip")
                                    .body(BodyInserters.fromFormData("code", data.getMovieUuid())
                                            .with("titleYN", "Y")
                                            .with("isOuterReq", "false")
                                            .with("CSRFToken", csrfToken))
                                    .retrieve()
                                    .bodyToMono(String.class)
                                    .block();

                            parseMovieDetail(detailPage,data);
                            Thread.sleep(300);
                        } catch (Exception e) {
                            log.error("Error processing movie detail for UUID: {}", data.getMovieUuid(), e);
                        }
                    }, executorService))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            executorService.shutdown();

            for (MovieInfoDto movieInfoDto : list) {
                MovieEntity movie = new MovieEntity(movieInfoDto);
                MovieEntity save = movieRepository.save(movie);
                CalendarMovieEntity calendarMovieEntity = new CalendarMovieEntity(calendarId, save.getMovieId(), movieInfoDto.getRanking());
                calendarMovieRepository.save(calendarMovieEntity);
            }
            startDate = startDate.plusDays(1);
            calendarId++;
        }
        // 2. movieEntities 사이즈 만큼 크롤링 시작
        // 3. 하나의 list에 5위까지의 데이터 수집 하고 각 데이터 마다 디테일 데이터 수집
    }

    private void parseMovieDetail(String html, MovieInfoDto data){
        Document document = Jsoup.parse(html);

        String imgPath = document.select("a.fl").attr("href");
        String content = document.select("p.desc_info").text();

        downloadImage(imgPath,data);
        data.setContent(content);

    }

    private void downloadImage(String imgPath, MovieInfoDto data) {
        final String pullPath = movie_api_url + imgPath;
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + ".webp";
        String filePath = file_path + fileName;

        try {
            byte[] imgBytes = webClient.get()
                    .uri(pullPath)
                    .header("Accept-Encoding", "gzip")
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path,imgBytes);
            data.setImg(fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseMovieRanking(String html, ArrayList<MovieInfoDto> list, ArrayList<CalendarMovieItem> calendarMovieEntities, Long calendarId) {
        Document document = Jsoup.parse(html);

        // 1. 랭킹, 2. 영화명, 3. 영화id

        String date = document.select("div.board_tit h4").text();
        log.info("Date = {}",date);
        int size = document.select("table.th_sort tbody tr").size();
        int time = 0;
        for (int i = 0; i < size; i++) {
            if (time >= 5){
                break;
            }
            Element element = document.select("table.th_sort tbody tr").get(i);
            String ranking = element.select("td").get(0).text();
            String movieName = element.select("td.tal span a").text();
            String onClick = element.select("td.tal span a").attr("onClick");
            String movieId = onClick.replaceAll(".*mstView\\('movie','(\\d+)'\\).*", "$1");
            String openDate = Optional.ofNullable(element.select("td").get(2).text())
                    .filter(text -> !text.isEmpty())
                    .orElse(null);

            MovieInfoDto movieInfoDto = new MovieInfoDto(movieName, movieId, ranking,openDate);


            list.add(movieInfoDto);
            CalendarMovieItem calendarMovieItem = new CalendarMovieItem(calendarId, movieId, ranking);
            calendarMovieEntities.add(calendarMovieItem);
            time++;
        }

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
