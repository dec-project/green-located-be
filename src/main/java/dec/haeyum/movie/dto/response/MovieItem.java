package dec.haeyum.movie.dto.response;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MovieItem {

    private Long movieId;
    private Integer ranking;
    private String title;
    private String img;

    protected MovieItem(Long movieId, Integer ranking, String title, String img, String file_url) {
        this.movieId = movieId;
        this.ranking = ranking;
        this.title = title;
        this.img = file_url+img;
    }

    public static List<MovieItem> asList(List<Top5MoviesDto> list, String file_url) {
        return list.stream()
                .map(dto -> new MovieItem(dto.getMovieId(),
                        dto.getRanking(),
                        dto.getTitle(),
                        dto.getImg(),
                        file_url))
                .collect(Collectors.toList());
    }
}
