package dec.haeyum.member.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetFavoriteListResponseDto {

    private List<FavoriteItem> itemList = new ArrayList<>();

    public GetFavoriteListResponseDto(List<FavoriteItem> favorite) {
        this.itemList.addAll(favorite);
    }


    public static ResponseEntity<GetFavoriteListResponseDto> success(List<FavoriteItem> itemList) {
        GetFavoriteListResponseDto result = new GetFavoriteListResponseDto(itemList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
