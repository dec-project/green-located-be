package dec.haeyum.movie.dto;

import dec.haeyum.movie.dto.response.MovieDbKeyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class MovieInfoDto {

    private String movieUuid;
    private Integer ranking;
    private LocalDate openDate;
    private String title;

    private String img;
    private String content;

    public MovieInfoDto(String movieName, String movieId, String ranking, String openDate) {
        this.movieUuid = movieId;
        this.title = movieName;
        this.ranking = Integer.parseInt(ranking);
        if (openDate != null && !openDate.trim().isEmpty()){
            this.openDate = LocalDate.parse(openDate);
        }else {
            this.openDate = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null ) return false;
        MovieDbKeyDto that = (MovieDbKeyDto) o;
        if (this.getMovieUuid().equals(that.getMovieUuid())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieUuid);
    }
}
