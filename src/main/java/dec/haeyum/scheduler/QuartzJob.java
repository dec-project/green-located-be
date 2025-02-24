package dec.haeyum.scheduler;

import dec.haeyum.calendar.dto.response.GetInitCalendarResponseDto;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.movie.service.impl.MovieServiceImplV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class QuartzJob implements Job {

    private final CalendarService calendarService;
    private final MovieServiceImplV2 movieService;



    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final LocalDate updateDate = LocalDate.now().minusDays(1);
        log.info("Quartz Job 실행 = {}", LocalDateTime.now());

        try {
            // 달력 init
            ResponseEntity<? super GetInitCalendarResponseDto> responseEntity = calendarService.initCalendar(updateDate);
            log.info("Quartz calendar init = {}",responseEntity.getStatusCode());
            // 영화 init
            movieService.crawlingMovie(updateDate);
            log.info("Quartz movie init success");
            // 음악 init

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
