package dec.haeyum.movie.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "movie")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    private String title;
    private String content;
    private LocalDate date;
    private String img;
    private String youtube_address;
    @OneToMany(mappedBy = "movieId")
    private List<CalendarMovieEntity> calendarMovies = new ArrayList<>();

}
