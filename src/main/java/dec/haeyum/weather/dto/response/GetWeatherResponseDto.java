package dec.haeyum.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class GetWeatherResponseDto  {

    private String weather;
    private String imgUrl;


    // 200
    public static ResponseEntity<GetWeatherResponseDto> success(GetWeatherResponseDto dto){
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

}
