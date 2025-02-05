package dec.haeyum.calendar.service;

import dec.haeyum.calendar.dto.request.PostCalendarRequestDto;
import dec.haeyum.calendar.dto.response.GetInitCalendarResponseDto;
import dec.haeyum.calendar.dto.response.PostCalendarResponseDto;
import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarService {

    // 달력 범위 조회
    ResponseEntity<? super PostCalendarResponseDto> getCalendar(PostCalendarRequestDto dto);
    // 달력 DB 초기화
    ResponseEntity<? super GetInitCalendarResponseDto> initCalendar(LocalDate endDate);
    // 달력 검증
    boolean validateCalendar(Long calendarId);
    // 특정 달력 조회
    CalendarEntity getCalendar(Long calendarId);
    //ViewCount 증가
    void increaseViewCount(CalendarEntity calendarId);

    CalendarEntity getCalendarForUpdate(Long calendarId);

    CalendarEntity getCalendar(LocalDate startDate);

    Boolean existedCalendar();
}
