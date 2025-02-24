package dec.haeyum.movie.service;

import dec.haeyum.movie.dto.response.GetMovieDetailResponseDto;
import dec.haeyum.movie.dto.response.GetTop5Movies;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface MovieService {
    ResponseEntity<GetTop5Movies> getTop5Movies(Long calendarId);

    ResponseEntity<GetMovieDetailResponseDto> getMovieDetail(Long calendarId, Long movieId);
    // movieId 존재 여부

    void crawlingMovie(LocalDate endDate);


}
