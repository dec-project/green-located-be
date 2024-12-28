package dec.haeyum.calendar.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetInitCalendarResponseDto {


    public static ResponseEntity<? super GetInitCalendarResponseDto> success(){
        GetInitCalendarResponseDto responseDto = new GetInitCalendarResponseDto();
        return ResponseEntity.ok().build();
    }


}


