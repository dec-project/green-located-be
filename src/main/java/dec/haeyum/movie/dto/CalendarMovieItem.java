package dec.haeyum.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarMovieItem {

    private Long calendarId;
    private String movieUuid;
    private Integer ranking;


    public CalendarMovieItem(Long calendarId, String movieUuid, String ranking) {
        this.calendarId = calendarId;
        this.movieUuid = movieUuid;
        this.ranking = Integer.parseInt(ranking);
    }
}
