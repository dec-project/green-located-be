package dec.haeyum.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class MovieInfoDto {

    private Integer movieUuid;
    private Integer ranking;
    private LocalDate openDate;
    private String title;

    private String img;
    private String director;
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieInfoDto that = (MovieInfoDto) o;
        return Objects.equals(movieUuid, that.movieUuid) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieUuid);
    }
}
