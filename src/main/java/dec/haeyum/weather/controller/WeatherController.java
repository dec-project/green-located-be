package dec.haeyum.weather.controller;

import dec.haeyum.weather.dto.request.PostWeatherImgRequestDto;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.PostWeatherImgResponseDto;
import dec.haeyum.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    @Operation(summary = "날씨 조회", description = "날씨 API에 날짜 전송 후 날씨 데이터 수집 후 반환")
    @GetMapping("/search/{calendarId}/weather")
    public ResponseEntity<GetWeatherResponseDto> getWeather(@PathVariable("calendarId") Long calendarId){
        ResponseEntity<GetWeatherResponseDto> result = weatherService.getWeather(calendarId);
        return result;
    }
    //@Operation(summary = "날씨 이미지 삽입")
    @PostMapping(value = "/weather/img")
    public ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(
            @RequestParam("weatherImgName") String imgName, @RequestParam("weatherImg")MultipartFile img){
        log.info("imageName ={}, img ={}",imgName,img);
        ResponseEntity<PostWeatherImgResponseDto> result = weatherService.setWeatherImg(imgName, img);
        return result;
    }

//
//    @Operation(summary = "날씨 이미지 삽입")
//    @PostMapping(value = "/weather/img",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(@ModelAttribute PostWeatherImgRequestDto dto){
//        log.info("img ={} imgName ={}",dto.getWeatherImgName(),dto.getWeatherImg().getOriginalFilename());
//        ResponseEntity<PostWeatherImgResponseDto> result = weatherService.setWeatherImg(dto);
//        return result;
//    }


}
