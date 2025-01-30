package dec.haeyum.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrowlingMovieData {

    private String movieId;
    private Integer ranking;
    private String movieName;

    public CrowlingMovieData(String ranking, String movieName, String movieId) {
        this.movieId = movieId;
        this.ranking = Integer.parseInt(ranking);
        this.movieName = movieName;
    }
}
