package dec.haeyum.movie.dto.response;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetTop5Movies {

    private List<MovieItem> itemList = new ArrayList<>();

    public GetTop5Movies(List<Top5MoviesDto> list, String file_url) {
        this.itemList = MovieItem.asList(list,file_url);
    }


    public static ResponseEntity<GetTop5Movies> success(List<Top5MoviesDto> list, String file_url){
        GetTop5Movies result = new GetTop5Movies(list, file_url);
        return ResponseEntity.ok(result);
    }

}
