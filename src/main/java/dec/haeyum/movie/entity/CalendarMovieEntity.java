package dec.haeyum.movie.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.*;

@Entity(name = "calendar_movie")
@IdClass(MoviePk.class)
public class CalendarMovieEntity {

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private CalendarEntity calendarId;
    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity movieId;
    private Integer ranking;
}
