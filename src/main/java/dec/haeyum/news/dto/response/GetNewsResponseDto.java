package dec.haeyum.news.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@Getter
public class GetNewsResponseDto {

    List<NewsItem> itemList = new ArrayList<>();

    public GetNewsResponseDto(List<NewsItem> itemList) {
        this.itemList = itemList;
    }


    public static ResponseEntity<GetNewsResponseDto> success(List<NewsItem> itemList) {
        GetNewsResponseDto result = new GetNewsResponseDto(itemList);
        return ResponseEntity.ok().body(result);
    }
}
