package dec.haeyum.movie.dto.response;

import dec.haeyum.movie.entity.MovieEntity;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@Getter
public class GetMovieDetailResponseDto {

    private String title;
    private String content;
    private LocalDate releaseDate;
    private String youtubeAddr;

    public GetMovieDetailResponseDto(MovieEntity movie) {
        this.title = movie.getTitle();
        this.content = movie.getContent();
        this.releaseDate = movie.getOpenDate();
        this.youtubeAddr = "https://www.youtube.com/embed/" + movie.getYoutube_address();
    }


    public static ResponseEntity<GetMovieDetailResponseDto> success(MovieEntity movie) {

        GetMovieDetailResponseDto result = new GetMovieDetailResponseDto(movie);
        return ResponseEntity.ok(result);
    }
}
