package dec.haeyum.movie.service;

import dec.haeyum.movie.dto.response.GetTop5Movies;
import org.springframework.http.ResponseEntity;

public interface MovieService {
    ResponseEntity<GetTop5Movies> getTop5Movies(Long calendarId);
    // movieId 존재 여부

}
