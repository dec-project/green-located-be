package dec.haeyum.searchCalender.service.impl;

import dec.haeyum.searchCalender.dto.request.PostCalenderDto;
import dec.haeyum.searchCalender.dto.request.PostCalenderRequestDto;
import dec.haeyum.searchCalender.dto.response.GetInitCalenderResponseDto;
import dec.haeyum.searchCalender.dto.response.PostCalenderResponseDto;
import dec.haeyum.searchCalender.entity.CalenderEntity;
import dec.haeyum.searchCalender.repository.CalenderRepository;
import dec.haeyum.searchCalender.service.CalenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalenderServiceImpl implements CalenderService {

    private final CalenderRepository calenderRepository;

    @Override
    @Transactional
    public ResponseEntity<? super GetInitCalenderResponseDto> initCalender(String updateDate) {
        LocalDate startDate = LocalDate.of(1970,1,1);
        try {
            // endDate = 업데이트 하려는 캘린더 날짜
            LocalDate endDate = LocalDate.parse(updateDate);

            // 1. DB에 가장 마지막 캘린더 날짜 조회
            CalenderEntity calenderEntity = searchDatabase();
            if (calenderEntity != null){
                LocalDate localDate = changeDate(calenderEntity);
                // 입력 -> 2000-01-01 , DB 마지막날이 2000-01-01
                if (localDate.isEqual(endDate)){
                    return GetInitCalenderResponseDto.duplicatedDate();
                }
            }
            // 2. endDate 까지 누락된 달력 생성
            createCalender(startDate,endDate);

        }catch (DateTimeParseException e){
            return GetInitCalenderResponseDto.dateTimeParseException();
        }catch (Exception e){
            return GetInitCalenderResponseDto.serverError();
        }
        return GetInitCalenderResponseDto.success();
    }


    @Override
    public ResponseEntity<? super PostCalenderResponseDto> getCalender(PostCalenderRequestDto dto) {
        Page<CalenderEntity> paging = Page.empty();
        try {
            PageRequest pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "calender_id"));
            // startDate와 일치하는 calenderName 을 찾고 endDate와 일치하는 calenderName을 찾아서 두 사이에 있는 데이터 다 가져오기
            paging = calenderRepository.findByCalenderNameBetween(dto.getStartDate(), dto.getEndDate(),pageable);
            if (paging.isEmpty()){
                return PostCalenderResponseDto.notExistedCalender();
            }
            if (paging.getTotalElements() > 90){
                return PostCalenderResponseDto.notExistedBoundary();
            }
        }catch (Exception e){
            return PostCalenderResponseDto.serverError();
        }
        return PostCalenderResponseDto.success(paging);
    }

    private PostCalenderDto parseString(PostCalenderRequestDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        String startDate = dto.getStartDate().format(formatter);
        String endDate = dto.getEndDate().format(formatter);
        PostCalenderDto postCalenderDto = new PostCalenderDto(startDate, endDate);
        return postCalenderDto;
    }


    private void createCalender(LocalDate startDate, LocalDate endDate) {
        List<CalenderEntity> list = new ArrayList<>();
        int size = 400;
        while (!startDate.isAfter(endDate)){
            CalenderEntity calenderEntity = new CalenderEntity();
            calenderEntity.createCalender(startDate);
            list.add(calenderEntity);
            if (list.size() == size){
                calenderRepository.saveAll(list);
                list.clear();
            }
            startDate = startDate.plusDays(1);
        }
        if (!list.isEmpty()){
            calenderRepository.saveAll(list);
        }
    }

    private LocalDate changeDate(CalenderEntity calenderEntity) {
        // 2001년 1월 2일
        String calenderName = calenderEntity.getCalenderName();
        // LocalDate로 변환
        String parseCalender = calenderName
                .replace("년", "-")
                .replace("월", "-")
                .replace("일", "")
                .replaceAll("\\s+", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate parse = LocalDate.parse(parseCalender, formatter);
        return parse;
    }

    private CalenderEntity  searchDatabase() {
        // 1. 달력 DB 가장 최신 일 조회
        return calenderRepository.findByLastData();
    }

}
