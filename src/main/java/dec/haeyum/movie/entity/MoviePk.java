package dec.haeyum.movie.entity;

import jakarta.persistence.Column;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MoviePk implements Serializable {

    @Column(name = "calendar_id")
    private Long calendarId;
    @Column(name = "movie_id")
    private Long movieId;

}
