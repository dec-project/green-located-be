package dec.haeyum.movie.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieInfoDto {

    private Integer movieId;
    private Integer ranking;
    private LocalDate openDate;
    private String title;

    private String img;
    private String director;
}
