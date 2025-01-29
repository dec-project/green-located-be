package dec.haeyum.weather.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.img.service.ImgService;
import dec.haeyum.weather.dto.request.PostWeatherImgRequestDto;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.PostWeatherImgResponseDto;
import dec.haeyum.weather.dto.response.WeatherApiResponseDto;
import dec.haeyum.weather.entity.WeatherEntity;
import dec.haeyum.weather.entity.WeatherImgEntity;
import dec.haeyum.weather.repository.WeatherImgRepository;
import dec.haeyum.weather.repository.WeatherRepository;
import dec.haeyum.weather.service.WeatherService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherImgRepository weatherImgRepository;
    private final CalendarService calendarService;
    private final ImgService imgService;
    private WebClient webClient;

    @Value("${weather.service-key}")
    private String serviceKey ;
    @Value("${weather.api-url}")
    private String weather_api_url;
    @Value("${spring.file.filePath}")
    private String filePath;
    @Value("${spring.file.fileUrl}")
    private String fileUrl;


    @PostConstruct
    public void initWebClient(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(weather_api_url);;
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(weather_api_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<GetWeatherResponseDto> getWeather(Long calendarId) {
        log.info("WeatherService::getWeather::start");
        GetWeatherResponseDto responseDto = new GetWeatherResponseDto();
        // DB 조회
        CalendarEntity calendar = calendarService.getCalendar(calendarId);
        // 달력에 날씨 데이터 없을 경우 날씨 API 호출하여 데이터 수집
        if (calendar.getWeather() == null){
            WeatherApiResponseDto weatherApiResponseDto = GetWeatherApiCall(calendar.getCalendarDate());
            WeatherEntity weatherEntity = new WeatherEntity(weatherApiResponseDto);
            calendar.setWeather(weatherEntity);
            responseDto.setWeather(weatherEntity.getWeatherName());
        }else {
            // 달력에 날씨 데이터 있을 경우 그대로 반환
            responseDto.setWeather(calendar.getWeather().getWeatherName());
        }
        WeatherImgEntity weatherImgEntity = weatherImgRepository.findByWeatherImgName(responseDto.getWeather())
                .orElse(null);
        log.info("weatherImgEntity ={}",weatherImgEntity.toString());
        if (weatherImgEntity == null){
            throw new BusinessException(ErrorCode.NOT_EXISTED_WEATHERIMG);
        }
        String imgPath = fileUrl + weatherImgEntity.getWeatherImg();
        log.info("imgPath=P}",imgPath);
        responseDto.setImgUrl(imgPath);
        return GetWeatherResponseDto.success(responseDto);
    }

    @Override
    // 날씨 이미지 저장
    public ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(PostWeatherImgRequestDto dto) {

        WeatherImgEntity weatherImgEntity = weatherImgRepository.findByWeatherImgName(dto.getWeatherImgName())
                .orElse(null);
        String uuidFileName = saveFile(dto.getWeatherImg());
        if (weatherImgEntity == null){
            WeatherImgEntity saveWeatherImgEntity = new WeatherImgEntity(dto.getWeatherImgName(), uuidFileName);
            weatherImgRepository.save(saveWeatherImgEntity);
        }else {
            deleteFile(weatherImgEntity.getWeatherImg());
            weatherImgEntity.setWeatherImg(uuidFileName);
        }
        return PostWeatherImgResponseDto.success();
    }

    @Override
    public ResponseEntity<PostWeatherImgResponseDto> setWeatherImg(String imgName, MultipartFile img) {

        WeatherImgEntity weatherImgEntity = weatherImgRepository.findByWeatherImgName(imgName)
                .orElse(null);
        String uuidFileName = saveFile(img);
        if (weatherImgEntity == null){
            WeatherImgEntity saveWeatherImgEntity = new WeatherImgEntity(imgName, uuidFileName);
            weatherImgRepository.save(saveWeatherImgEntity);
        }else {
            deleteFile(weatherImgEntity.getWeatherImg());
            weatherImgEntity.setWeatherImg(uuidFileName);
            weatherImgRepository.save(weatherImgEntity);
        }
        return PostWeatherImgResponseDto.success();
    }

    private void deleteFile(String weatherImg) {
        String imgFilePath = filePath + weatherImg;
        Path path = Paths.get(imgFilePath);
        try {
            Files.deleteIfExists(path);
        }catch (IOException e){
            throw new BusinessException(ErrorCode.CANT_DELETE_FILE);
        }
    }

    private String saveFile(MultipartFile weatherImg) {
        MultipartFile img = weatherImg;
        String imgName = imgService.downloadImg(img);
        return imgName;
    }


    private WeatherApiResponseDto GetWeatherApiCall(LocalDate searchDate) {
        WeatherApiResponseDto weatherApiResponseDto = new WeatherApiResponseDto();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formatDate = searchDate.format(formatter);
            String json = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("dataType", "JSON")
                            .queryParam("dataCd", "ASOS")
                            .queryParam("dateCd", "DAY")
                            .queryParam("startDt", formatDate)
                            .queryParam("endDt", formatDate)
                            .queryParam("stnIds", "108")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper rootNode = new ObjectMapper();
            JsonNode node = rootNode.readTree(json);
            JsonNode result = node.path("response").path("body").path("items").path("item");
            if (result.isArray()) {
                weatherApiResponseDto.setData(result);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return weatherApiResponseDto;

    }
}
