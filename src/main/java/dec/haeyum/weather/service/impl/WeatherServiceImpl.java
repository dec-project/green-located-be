package dec.haeyum.weather.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.weather.dto.response.GetWeatherResponseDto;
import dec.haeyum.weather.dto.response.WeatherApiResponseDto;
import dec.haeyum.weather.entity.WeatherEntity;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final CalendarService calendarService;
    private WebClient webClient;

    @Value("${weather.service-key}")
    private String serviceKey ;
    @Value("${weather.api-url}")
    private String weather_api_url;


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
    public ResponseEntity<? super GetWeatherResponseDto> getWeather(Long calendarId) {
        String weather;
        try {
            // DB 조회
            Optional<CalendarEntity> calendarEntityOptional = calendarService.getCalendar(calendarId);
            if (calendarEntityOptional.isEmpty()){
                throw new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR);
            }
            CalendarEntity calendar = calendarEntityOptional.get();
            // 달력에 날씨 데이터 없을 경우 날씨 API 호출하여 데이터 수집
            if (calendar.getWeather() == null){
                WeatherApiResponseDto weatherApiResponseDto = GetWeatherApiCall(calendar.getCalendarDate());
                WeatherEntity weatherEntity = new WeatherEntity(weatherApiResponseDto);
                calendar.setWeather(weatherEntity);
                return GetWeatherResponseDto.success(weatherEntity.getWeatherName());
            }else {
                // 달력에 날씨 데이터 있을 경우 그대로 반환
                weather = calendar.getWeather().getWeatherName();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return GetWeatherResponseDto.success(weather);
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
