package dec.haeyum.calendar.dto.response;

import lombok.Getter;

@Getter
public class Top5MoviesDto {

    private Long movieId;
    private Integer ranking;
    private String title;
    private String img;

}
