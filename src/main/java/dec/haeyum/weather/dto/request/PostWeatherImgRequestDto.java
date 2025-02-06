package dec.haeyum.weather.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostWeatherImgRequestDto {
    private MultipartFile weatherImg;
    private String weatherImgName;

}
