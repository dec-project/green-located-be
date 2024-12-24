package dec.haeyum.searchCalender.controller;

import dec.haeyum.searchCalender.dto.request.PostCalenderRequestDto;
import dec.haeyum.searchCalender.dto.response.GetInitCalenderResponseDto;
import dec.haeyum.searchCalender.dto.response.PostCalenderResponseDto;
import dec.haeyum.searchCalender.service.CalenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService service;

    @PostMapping("/calender")
    public ResponseEntity<? super PostCalenderResponseDto> calender(@Valid @RequestBody PostCalenderRequestDto dto){
        ResponseEntity<? super PostCalenderResponseDto> responseEntity = service.getCalender(dto);
        return responseEntity;


    }

    // 달력 최신화
    @GetMapping("/calender/generate")
    public ResponseEntity<? super GetInitCalenderResponseDto> initCalender(@RequestParam String endDate){
        ResponseEntity<? super GetInitCalenderResponseDto> responseEntity = service.initCalender(endDate);
        return responseEntity;

    }

}
