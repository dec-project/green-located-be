package dec.haeyum.movie.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "calendar_movie")
@IdClass(MoviePk.class)
@NoArgsConstructor
@Getter
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

    public CalendarMovieEntity(Long calendarId, Long movieId, Integer ranking) {
        this.calendarId = calendarId;
        this.movieId = movieId;
        this.ranking = ranking;

    }

    public CalendarMovieEntity(Long calendarId, Long movieId, String ranking) {
        this.calendarId = calendarId;
        this.movieId = movieId;
        this.ranking = Integer.parseInt(ranking);
    }
}
