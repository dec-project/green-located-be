package dec.haeyum.movie.controller;

import dec.haeyum.movie.dto.response.GetMovieDetailResponseDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class MovieController {

    public MovieController(@Qualifier("movieServiceImplV2") MovieService movieService) {
        this.movieService = movieService;
    }

    private MovieService movieService;

    @Operation(summary = "TOP 5 영화 조회", description = "해당 캘린더 날짜의 TOP 5 영화 크롤링 및 데이터 수집 후 반환")
    @GetMapping("/search/{calendarId}/movies")
    public ResponseEntity<GetTop5Movies> getTop5Movies(@PathVariable("calendarId")Long calendarId){
        ResponseEntity<GetTop5Movies> result = movieService.getTop5Movies(calendarId);
        return result;
    }
    @Operation(summary = "TOP 5 영화 상세 조회",description = "해당 캘린더 날짜의 특정 영화 예고편, 개봉날짜, 내용 조회")
    @GetMapping("/search/{calendarId}/movies/{movieId}")
    public ResponseEntity<GetMovieDetailResponseDto> getMovieDetail(@PathVariable(name = "calendarId") Long calendarId, @PathVariable(name = "movieId") Long movieId){
        ResponseEntity<GetMovieDetailResponseDto> result = movieService.getMovieDetail(calendarId, movieId);

        return result;
    }

    @GetMapping("/movie/generate")
    public String movieGenerate(@RequestParam String endDate){
        movieService.crawlingMovie(endDate);
        log.error("영화 데이터 생성 성공");
        return "영화 데이터 생성 성공";
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon(){
    }

}
