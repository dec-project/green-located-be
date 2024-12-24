package dec.haeyum.searchCalender.dto;

import dec.haeyum.searchCalender.dto.response.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class ResponseDto {

    private String code;
    private String message;

    public ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<ResponseDto> serverError(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.serverError, ResponseMessage.serverError);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
}
