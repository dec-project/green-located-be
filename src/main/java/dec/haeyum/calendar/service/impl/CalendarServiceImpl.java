package dec.haeyum.calendar.service.impl;

import dec.haeyum.calendar.dto.request.PostCalendarDto;
import dec.haeyum.calendar.dto.request.PostCalendarRequestDto;
import dec.haeyum.calendar.dto.response.GetInitCalendarResponseDto;
import dec.haeyum.calendar.dto.response.PostCalendarResponseDto;
import dec.haeyum.calendar.dto.response.ResponseCalendarDto;
import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.chat.service.ChatService;
import dec.haeyum.chat.service.ChatroomService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.movie.entity.MovieEntity;
import dec.haeyum.song.service.SongService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public  class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final SongService songService;
    private final ChatroomService chatroomService;

    @Override
    @Transactional
    public ResponseEntity<? super GetInitCalendarResponseDto> initCalendar(LocalDate endDate) {
        LocalDate startDate = LocalDate.of(1970,1,1);
        try {
            // 1. DB에 가장 마지막 캘린더 날짜 조회
            CalendarEntity calendarEntity = searchDatabase();
            if (calendarEntity != null){
                startDate = calendarEntity.getCalendarDate();
            }
            log.info("startDate = {} , updateDate = {}",startDate, endDate);


            if (calendarEntity != null){
                // 입력 -> 2000-01-01 , DB 마지막날이 2000-01-01
                LocalDate localDate = calendarEntity.getCalendarDate();
                // startDate == updateDate 가 같다면
                if (localDate.isEqual(endDate)){
                    throw new BusinessException(ErrorCode.DUPLICATED_CALENDAR_DATE);
                }
                // updateDate가 DB 데이터보다 이전이면
                if (endDate.isBefore(localDate)){
                    throw new BusinessException(ErrorCode.DUPLICATED_CALENDAR_DATE);
                }
            }
            // 2. endDate 까지 누락된 달력 생성
            createCalendar(startDate,endDate);
        }catch (DateTimeParseException e){
            throw new BusinessException(ErrorCode.NOT_EXISTED_DATETIME_PARSE);
        }
        return GetInitCalendarResponseDto.success();
    }


    @Override
    @Transactional
    public ResponseEntity<PostCalendarResponseDto> getCalendar(PostCalendarRequestDto dto) {
        final int MAX_BOUNDARY = 90;
        Page<CalendarEntity> paging;
        PageRequest pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "calendar_id"));
        paging = calendarRepository.findByCalendarNameBetween(dto.getStartDate(), dto.getEndDate(),pageable);

        validatePagingData(paging, MAX_BOUNDARY);

        List<ResponseCalendarDto> responseItems = new ArrayList<>();

        for (CalendarEntity calendar : paging.getContent()) {
            Long chatroomId = chatroomService.getChatRoomIdByCalendar(calendar);
            String calendarSongImageUrl = songService.getCalendarSongImageUrl(calendar.getCalendarId());
            ResponseCalendarDto responseCalendarDto = new ResponseCalendarDto(calendar, calendarSongImageUrl, chatroomId);
            responseItems.add(responseCalendarDto);
        }

        return PostCalendarResponseDto.success(paging,responseItems);
    }

    private static void validatePagingData(Page<CalendarEntity> paging, int MAX_BOUNDARY) {
        if (paging.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR);
        }
        if (paging.getTotalElements() > MAX_BOUNDARY){
            throw new BusinessException(ErrorCode.NOT_EXISTED_BOUNDARY);
        }
    }

    // DB에 달력 있는지 검증
    public boolean validateCalendar(Long calendarId){
        return calendarRepository.findById(calendarId).isPresent();
    }

    // 특정 달력 DB 반환
    @Override
    public CalendarEntity getCalendar(Long calendarId) {
        return calendarRepository.findById(calendarId).orElseThrow(() ->  new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR));
    }


    // 특정 달력 DB 반환
    @Override
    public CalendarEntity getCalendarForUpdate(Long calendarId) {
        return calendarRepository.findByIdForUpdate(calendarId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR));
    }

    @Override
    public CalendarEntity getCalendar(LocalDate startDate) {
        return calendarRepository.findByCalendarDate(startDate)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR));
    }

    @Override
    public Boolean existedCalendar() {

        long count = calendarRepository.count();
        if (count == 0){
            return false;
        }
        return true;

    }


    @Override
    public void increaseViewCount(CalendarEntity calendar) {
        calendar.setViewCount(calendar.getViewCount() + 1);
        calendarRepository.save(calendar);
        calendarRepository.flush();
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

    // 달력 DB 가장 최신 일 조회
    private CalendarEntity searchDatabase() {
        return calendarRepository.findByLastData();
    }

}
