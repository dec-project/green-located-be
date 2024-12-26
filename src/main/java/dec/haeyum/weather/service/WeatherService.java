package dec.haeyum.weather.service;

import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import org.springframework.http.ResponseEntity;

public interface WeatherService {
    ResponseEntity<? super GetWeatherResponseDto> getWeather(Long calendarId);
}
