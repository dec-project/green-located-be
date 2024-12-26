package dec.haeyum.config.error;

import dec.haeyum.config.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessException e) {
        return createErrorResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(BusinessException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(e.getErrorCode(), e.getMessage()),
                e.getErrorCode().getStatus());
    }

}
