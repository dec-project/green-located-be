package dec.haeyum.movie.controller;

import dec.haeyum.movie.dto.response.GetMovieDetailResponseDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import dec.haeyum.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/search/{calendarId}/movies")
    public ResponseEntity<GetTop5Movies> getTop5Movies(@PathVariable("calendarId")Long calendarId){
        ResponseEntity<GetTop5Movies> result = movieService.getTop5Movies(calendarId);
        return result;
    }

    @GetMapping("/search/{calendarId}/movies/{movieId}")
    public ResponseEntity<GetMovieDetailResponseDto> getMovieDetail(@PathVariable(name = "calendarId") Long calendarId, @PathVariable(name = "movieId") Long movieId){
        ResponseEntity<GetMovieDetailResponseDto> result = movieService.getMovieDetail(calendarId, movieId);

        return result;
    }



}
