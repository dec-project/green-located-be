package dec.haeyum.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "존재하지 않는 songId입니다.");

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


}
