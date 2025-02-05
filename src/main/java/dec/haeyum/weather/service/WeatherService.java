package dec.haeyum.weather.service;

import dec.haeyum.weather.dto.request.PostWeatherImgRequestDto;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.PostWeatherImgResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface WeatherService {
    ResponseEntity<GetWeatherResponseDto> getWeather(Long calendarId);

    ResponseEntity<PostWeatherImgResponseDto> initWeatherImg(PostWeatherImgRequestDto dto);

    ResponseEntity<PostWeatherImgResponseDto> initWeatherImg(String imgName, MultipartFile img);
    Boolean existedWeatherImg();
    void initWeatherImg();
}
