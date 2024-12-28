package dec.haeyum.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class GetWeatherResponseDto  {

    private String weather;

    public GetWeatherResponseDto(String weather) {
        this.weather = weather;
    }

    // 200
    public static ResponseEntity<? super GetWeatherResponseDto> success(String weather){
        GetWeatherResponseDto result = new GetWeatherResponseDto(weather);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    // 200 - 날씨 데이터 없음
    public static ResponseEntity<? super GetWeatherResponseDto> success(){
        GetWeatherResponseDto result = new GetWeatherResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
