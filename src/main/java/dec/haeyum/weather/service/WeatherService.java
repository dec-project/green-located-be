package dec.haeyum.weather.service;

import dec.haeyum.weather.dto.request.PostWeatherImgRequestDto;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.PostWeatherImgResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface WeatherService {
    ResponseEntity<GetWeatherResponseDto> getWeather(Long calendarId);

    ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(PostWeatherImgRequestDto dto);

    ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(String imgName, MultipartFile img);
}
