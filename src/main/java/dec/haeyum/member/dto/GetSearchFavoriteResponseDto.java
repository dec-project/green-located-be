package dec.haeyum.member.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetSearchFavoriteResponseDto {

    private Boolean isFavorite;

    public GetSearchFavoriteResponseDto(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public static ResponseEntity<GetSearchFavoriteResponseDto> success(boolean isFavorite) {
        GetSearchFavoriteResponseDto result = new GetSearchFavoriteResponseDto(isFavorite);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
