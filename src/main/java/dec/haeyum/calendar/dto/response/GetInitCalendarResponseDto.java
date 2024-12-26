package dec.haeyum.calendar.dto.response;

import dec.haeyum.calendar.dto.ResponseCode;
import dec.haeyum.calendar.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetInitCalendarResponseDto extends ResponseDto {


    public GetInitCalendarResponseDto() {
        super(ResponseCode.success,ResponseMessage.success);
    }

    public static ResponseEntity<ResponseDto> dateTimeParseException(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.notExistedDate, ResponseMessage.notExistedDate);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<? super GetInitCalendarResponseDto> success(){
        GetInitCalendarResponseDto responseDto = new GetInitCalendarResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public static ResponseEntity<? super GetInitCalendarResponseDto> duplicatedDate() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.duplicatedDate, ResponseMessage.duplicatedDate);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
