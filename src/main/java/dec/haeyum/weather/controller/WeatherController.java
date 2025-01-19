package dec.haeyum.weather.controller;

import dec.haeyum.weather.dto.request.PostWeatherImgRequestDto;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.PostWeatherImgResponseDto;
import dec.haeyum.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    @Operation(summary = "날씨 조회", description = "날씨 API에 날짜 전송 후 날씨 데이터 수집 후 반환")
    @GetMapping("/search/weather/{calendarId}")
    public ResponseEntity<GetWeatherResponseDto> getWeather(@PathVariable("calendarId") Long calendarId){
        ResponseEntity<GetWeatherResponseDto> result = weatherService.getWeather(calendarId);
        return result;
    }
    @Operation(summary = "날씨 이미지 삽입")
    @PostMapping("/weather/img")
    public ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(@ModelAttribute PostWeatherImgRequestDto dto){
        ResponseEntity<PostWeatherImgResponseDto> result = weatherService.setWeatherImg(dto);
        return result;
    }
}
