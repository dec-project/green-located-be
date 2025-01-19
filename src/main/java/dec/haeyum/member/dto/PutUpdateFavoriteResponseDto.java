package dec.haeyum.member.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PutUpdateFavoriteResponseDto {

    private Boolean isFavorite;

    public PutUpdateFavoriteResponseDto(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public static ResponseEntity<PutUpdateFavoriteResponseDto> success(boolean isFavorite) {
        PutUpdateFavoriteResponseDto result = new PutUpdateFavoriteResponseDto(isFavorite);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
