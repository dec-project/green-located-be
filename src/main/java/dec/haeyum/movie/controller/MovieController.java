package dec.haeyum.movie.controller;

import dec.haeyum.movie.dto.response.GetMovieDetailResponseDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

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
    @GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon(){
    }

}
