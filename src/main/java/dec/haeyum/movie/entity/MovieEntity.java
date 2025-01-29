package dec.haeyum.movie.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.external.youtube.dto.YoutubeDetailDto;
import dec.haeyum.movie.dto.MovieInfoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "movie")
@Getter
@Setter
@NoArgsConstructor
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    @Column(unique = true) // 예외 -> DataIntegrityViolationException
    private String movieUuid;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDate openDate;
    private String img;
    private String youtube_address;

    public MovieEntity(MovieInfoDto data) {
        this.movieUuid = data.getMovieUuid();
        this.openDate = data.getOpenDate();
        this.title = data.getTitle();
        this.img = data.getImg();
        this.content = data.getContent();
    }

    public void setYoutubeData(YoutubeDetailDto youtubeDetailDto) {
        this.youtube_address =  youtubeDetailDto.getYoutubeAddr();
    }

    public void setYoutubeAddress(YoutubeDetailDto dto){
        this.youtube_address = "https://www.youtube.com/embed/" + dto.getYoutubeAddr();
    }


}
