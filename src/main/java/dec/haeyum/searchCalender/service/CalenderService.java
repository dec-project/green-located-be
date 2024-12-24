package dec.haeyum.searchCalender.service;

import dec.haeyum.searchCalender.dto.request.PostCalenderRequestDto;
import dec.haeyum.searchCalender.dto.response.GetInitCalenderResponseDto;
import dec.haeyum.searchCalender.dto.response.PostCalenderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface CalenderService {


    ResponseEntity<? super PostCalenderResponseDto> getCalender(PostCalenderRequestDto dto);

    ResponseEntity<? super GetInitCalenderResponseDto> initCalender(String endDate);
}
