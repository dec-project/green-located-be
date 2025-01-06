package dec.haeyum.config.error;

import dec.haeyum.calendar.controller.CalendarController;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.movie.controller.MovieController;
import dec.haeyum.song.controller.SongController;
import dec.haeyum.weather.controller.WeatherController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessException e) {
        return createErrorResponseEntity(e);
    }

    // 예상하지 못한 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        e.printStackTrace();
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),errorCode.getStatus()
        );
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(BusinessException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(e.getErrorCode(), e.getMessage()),
                e.getErrorCode().getStatus());
    }

}
