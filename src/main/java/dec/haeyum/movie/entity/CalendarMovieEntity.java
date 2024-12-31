package dec.haeyum.movie.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity(name = "calendar_movie")
@IdClass(MoviePk.class)
@NoArgsConstructor
public class CalendarMovieEntity {

    @Id
    private Long calendarId;
    @Id
    private Long movieId;
    private Integer ranking;

    public CalendarMovieEntity(CalendarEntity calendar, MovieEntity movie, Integer ranking) {
        this.calendarId = calendar.getCalendarId();
        this.movieId = movie.getMovieId();
        this.ranking = ranking;
    }
}
