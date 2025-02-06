package dec.haeyum.config;

import dec.haeyum.calendar.dto.response.GetInitCalendarResponseDto;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.chat.repository.ChatRoomRepository;
import dec.haeyum.chat.service.ChatService;
import dec.haeyum.redis.RedisService;
import dec.haeyum.song.service.SongService;
import dec.haeyum.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements ApplicationRunner {

    private final CalendarService calendarService;
    private final WeatherService weatherService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final RedisService redisService;
    private final SongService songService;

    @Override
    public void run(ApplicationArguments args) {
        try {

            // 캘린더 DB에 데이터가 없을 경우 init
            Boolean isCalendarCheck = calendarService.existedCalendar();
            if (!isCalendarCheck){
                LocalDate currentDate = LocalDate.now();
                ResponseEntity<? super GetInitCalendarResponseDto> responseEntity = calendarService.initCalendar(currentDate);
                log.info("Calendar init ={}",responseEntity.getStatusCode());
                redisService.flushAll();
            }

            // 채팅방 데이터가 없을 경우 init
            long count = chatRoomRepository.count();
            if (count == 0){
                chatService.createChatRoom();
                log.info("chatRoom create success");
            }
            // 날씨 이미지가 없을 경우 init
            Boolean isWeatherImgCheck = weatherService.existedWeatherImg();
            if (!isWeatherImgCheck){
                weatherService.initWeatherImg();
                log.info("weatherImg create success");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
