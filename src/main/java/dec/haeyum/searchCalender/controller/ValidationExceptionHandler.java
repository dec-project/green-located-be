package dec.haeyum.searchCalender.controller;

import dec.haeyum.searchCalender.dto.ResponseCode;
import dec.haeyum.searchCalender.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex){
        ResponseDto responseDto = new ResponseDto();
        String defaultMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        if (defaultMessage.equals("ND")){
            responseDto = new ResponseDto(defaultMessage, "시작일 또는 도착일을 선택해주세요.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

}
