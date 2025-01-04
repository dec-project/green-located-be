package dec.haeyum.weather.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class PostWeatherImgResponseDto {

    public static ResponseEntity<PostWeatherImgResponseDto> success(){
        PostWeatherImgResponseDto dto = new PostWeatherImgResponseDto();
        return ResponseEntity.ok().build();
    }
}
