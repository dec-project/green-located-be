package dec.haeyum.movie.dto.response;

import dec.haeyum.movie.dto.MovieInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDbKeyDto {

    private Long movieId;
    private String movieUuid;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        MovieInfoDto that = (MovieInfoDto) o;
        if (this.movieUuid.equals(that.getMovieUuid())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieUuid);
    }
}
