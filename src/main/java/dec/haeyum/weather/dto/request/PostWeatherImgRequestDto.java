package dec.haeyum.weather.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class PostWeatherImgRequestDto {

    private String weatherImgName;
    private MultipartFile weatherImg;

}
