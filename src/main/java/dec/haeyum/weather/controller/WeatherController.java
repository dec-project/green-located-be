package dec.haeyum.weather.controller;

import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather/{calendarId}")
    public ResponseEntity<? super GetWeatherResponseDto> getWeather(@PathVariable("calendarId") Long calendarId){
        ResponseEntity<? super GetWeatherResponseDto> result = weatherService.getWeather(calendarId);
        return result;
    }
}
