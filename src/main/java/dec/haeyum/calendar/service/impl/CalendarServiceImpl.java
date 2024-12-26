package dec.haeyum.calendar.service.impl;

import dec.haeyum.calendar.dto.request.PostCalendarDto;
import dec.haeyum.calendar.dto.request.PostCalendarRequestDto;
import dec.haeyum.calendar.dto.response.GetInitCalendarResponseDto;
import dec.haeyum.calendar.dto.response.PostCalendarResponseDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;

    @Override
    @Transactional
    public ResponseEntity<? super GetInitCalendarResponseDto> initCalendar(String updateDate) {
        LocalDate startDate = LocalDate.of(1970,1,1);
        try {
            // endDate = 업데이트 하려는 캘린더 날짜
            LocalDate endDate = LocalDate.parse(updateDate);

            // 1. DB에 가장 마지막 캘린더 날짜 조회
            CalendarEntity calendarEntity = searchDatabase();
            if (calendarEntity != null){
                LocalDate localDate = changeDate(calendarEntity);
                // 입력 -> 2000-01-01 , DB 마지막날이 2000-01-01
                if (localDate.isEqual(endDate)){
                    return GetInitCalendarResponseDto.duplicatedDate();
                }
            }
            // 2. endDate 까지 누락된 달력 생성
            createCalendar(startDate,endDate);

        }catch (DateTimeParseException e){
            return GetInitCalendarResponseDto.dateTimeParseException();
        }catch (Exception e){
            return GetInitCalendarResponseDto.serverError();
        }
        return GetInitCalendarResponseDto.success();
    }


    @Override
    public ResponseEntity<? super PostCalendarResponseDto> getCalendar(PostCalendarRequestDto dto) {
        Page<CalendarEntity> paging = Page.empty();
        try {
            PageRequest pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "calendar_id"));
            // startDate와 일치하는 calendarName 을 찾고 endDate와 일치하는 calendarName을 찾아서 두 사이에 있는 데이터 다 가져오기
            paging = calendarRepository.findByCalendarNameBetween(dto.getStartDate(), dto.getEndDate(),pageable);
            if (paging.isEmpty()){
                return PostCalendarResponseDto.notExistedCalendar();
            }
            if (paging.getTotalElements() > 90){
                return PostCalendarResponseDto.notExistedBoundary();
            }
        }catch (Exception e){
            return PostCalendarResponseDto.serverError();
        }
        return PostCalendarResponseDto.success(paging );
    }

    // DB에 달력 있는지 검증
    public boolean validateCalendar(Long calendarId){
        CalendarEntity calendarEntity = calendarRepository.findById(calendarId)
                .orElse(null);
        if (calendarEntity == null){
            return false;
        }
        return true;
    }

    // 특정 달력 DB 반환
    @Override
    public CalendarEntity getCalendar(Long calendarId) {
        CalendarEntity calendarEntity = calendarRepository.findById(calendarId)
                .orElse(null);
        return calendarEntity;
    }


    private PostCalendarDto parseString(PostCalendarRequestDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        String startDate = dto.getStartDate().format(formatter);
        String endDate = dto.getEndDate().format(formatter);
        PostCalendarDto postCalendarDto = new PostCalendarDto(startDate, endDate);
        return postCalendarDto;
    }

    private void createCalendar(LocalDate startDate, LocalDate endDate) {
        List<CalendarEntity> list = new ArrayList<>();
        int size = 400;
        while (!startDate.isAfter(endDate)){
            CalendarEntity calendarEntity = new CalendarEntity();
            calendarEntity.createCalendar(startDate);
            list.add(calendarEntity);
            if (list.size() == size){
                calendarRepository.saveAll(list);
                list.clear();
            }
            startDate = startDate.plusDays(1);
        }
        if (!list.isEmpty()){
            calendarRepository.saveAll(list);
        }
    }

    private LocalDate changeDate(CalendarEntity calendarEntity) {
        // 2001년 1월 2일
        String calendarName = calendarEntity.getCalendarName();
        // LocalDate로 변환
        String parseCalendar = calendarName
                .replace("년", "-")
                .replace("월", "-")
                .replace("일", "")
                .replaceAll("\\s+", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate parse = LocalDate.parse(parseCalendar, formatter);
        return parse;
    }

    private CalendarEntity searchDatabase() {
        // 1. 달력 DB 가장 최신 일 조회
        return calendarRepository.findByLastData();
    }

}
