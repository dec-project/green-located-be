package dec.haeyum.movie.entity;

import jakarta.persistence.Column;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class MoviePk implements Serializable {

    @Column(name = "calendar_id")
    private Long calendarId;
    @Column(name = "movie_id")
    private Long movieId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviePk moviePk = (MoviePk) o;
        return Objects.equals(calendarId, moviePk.calendarId) && Objects.equals(movieId, moviePk.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarId, movieId);
    }
}
