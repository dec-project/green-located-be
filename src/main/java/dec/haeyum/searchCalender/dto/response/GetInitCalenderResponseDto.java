package dec.haeyum.searchCalender.dto.response;

import dec.haeyum.searchCalender.dto.ResponseCode;
import dec.haeyum.searchCalender.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetInitCalenderResponseDto extends ResponseDto {


    public GetInitCalenderResponseDto() {
        super(ResponseCode.success,ResponseMessage.success);
    }

    public static ResponseEntity<ResponseDto> dateTimeParseException(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.notExistedDate, ResponseMessage.notExistedDate);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<? super GetInitCalenderResponseDto> success(){
        GetInitCalenderResponseDto responseDto = new GetInitCalenderResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public static ResponseEntity<? super GetInitCalenderResponseDto> duplicatedDate() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.duplicatedDate, ResponseMessage.duplicatedDate);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
