package dec.haeyum.weather.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostWeatherImgRequestDto {

    private String weatherImgName;
    private MultipartFile weatherImg;

}
