package dec.haeyum.calendar.controller;

import dec.haeyum.calendar.dto.request.PostCalendarRequestDto;
import dec.haeyum.calendar.dto.response.GetInitCalendarResponseDto;
import dec.haeyum.calendar.dto.response.PostCalendarResponseDto;
import dec.haeyum.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public  class CalendarController {

    private final CalendarService service;

    @Operation(summary = "달력 검색", description = "startDate , endDate 범위 내의 달력 반환")
    @PostMapping("/calendar")
    public ResponseEntity<? super PostCalendarResponseDto> calendar(@Valid @RequestBody PostCalendarRequestDto dto){
        ResponseEntity<? super PostCalendarResponseDto> responseEntity = service.getCalendar(dto);
        return responseEntity;


    }

    @Operation(summary = "달력 최신화", description = "endDate 까지의 달력 갱신")
    @GetMapping("/calendar/generate")
    public ResponseEntity<? super GetInitCalendarResponseDto> initCalendar(@RequestParam String endDate){
        ResponseEntity<? super GetInitCalendarResponseDto> responseEntity = service.initCalendar(endDate);
        return responseEntity;

    }

}
