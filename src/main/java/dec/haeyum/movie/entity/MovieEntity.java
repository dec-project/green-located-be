package dec.haeyum.movie.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.movie.dto.MovieInfoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "movie")
@Getter
@NoArgsConstructor
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    @Column(unique = true) // 예외 -> DataIntegrityViolationException
    private Integer movieUuid;
    private String title;
    private String content;
    private LocalDate openDate;
    private String img;
    private String director;
    private String youtube_address;

    public MovieEntity(MovieInfoDto data) {
        this.movieUuid = data.getMovieUuid();
        this.openDate = data.getOpenDate();
        this.title = data.getTitle();
        this.img = data.getImg();
        this.director = data.getDirector();
    }
}
