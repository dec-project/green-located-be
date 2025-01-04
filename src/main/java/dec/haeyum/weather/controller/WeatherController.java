package dec.haeyum.weather.controller;

import dec.haeyum.weather.dto.request.PostWeatherImgRequestDto;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.PostWeatherImgResponseDto;
import dec.haeyum.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather/{calendarId}")
    public ResponseEntity<GetWeatherResponseDto> getWeather(@PathVariable("calendarId") Long calendarId){
        ResponseEntity<GetWeatherResponseDto> result = weatherService.getWeather(calendarId);
        return result;
    }

    @PostMapping("/weather/img")
    public ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(@ModelAttribute PostWeatherImgRequestDto dto){
        ResponseEntity<PostWeatherImgResponseDto> result = weatherService.setWeatherImg(dto);
        return result;
    }
}
