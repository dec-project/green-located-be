package dec.haeyum.news.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


@Getter
public class GetNewsResponseDto {

    List<NewsItem> itemList = new ArrayList<>();

    public GetNewsResponseDto(List<NewsItem> itemList) {
        this.itemList = itemList;
    }

    public GetNewsResponseDto(ConcurrentLinkedQueue<NewsItem> itemList) {
        this.itemList.addAll(itemList);
    }


    public static ResponseEntity<GetNewsResponseDto> success(List<NewsItem> itemList) {
        GetNewsResponseDto result = new GetNewsResponseDto(itemList);
        return ResponseEntity.ok().body(result);
    }

    public static ResponseEntity<GetNewsResponseDto> success(ConcurrentLinkedQueue<NewsItem> itemList) {
        GetNewsResponseDto result = new GetNewsResponseDto(itemList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
