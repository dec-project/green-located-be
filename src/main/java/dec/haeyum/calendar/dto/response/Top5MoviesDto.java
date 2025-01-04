package dec.haeyum.calendar.dto.response;
import lombok.Getter;

@Getter
public class Top5MoviesDto {


    private Long movieId;
    private Integer ranking;
    private String title;
    private String img;


    public Top5MoviesDto(Long movieId, Integer ranking, String title, String img) {
        this.movieId = movieId;
        this.ranking = ranking;
        this.title = title;
        this.img = img;
    }
}
