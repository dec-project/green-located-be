package dec.haeyum.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SONG_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "존재하지 않는 songId입니다."),
    // 공통 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"ISE","서버 처리 중 오류가 발생했습니다. 관리자에게 문의하세요."),
    // Calendar 에러
    NOT_EXISTED_BOUNDARY(HttpStatus.NOT_FOUND,"EB","검색할 수 있는 범위를 초과했습니다."),
    DUPLICATED_CALENDAR_DATE(HttpStatus.BAD_REQUEST,"DC","이미 가장 최신 달력입니다."),
    NOT_EXISTED_DATETIME_PARSE(HttpStatus.BAD_REQUEST,"ED","유효하지 않은 날짜 형식입니다. ex)2024-12-25"),
    NOT_EXISTED_CALENDAR(HttpStatus.NOT_FOUND,"EC","찾을 수 있는 달력이 없습니다."),
    // 영화 에러
    NOT_CONNECT_PAGE(HttpStatus.INTERNAL_SERVER_ERROR,"NC","영화 페이지 를 찾을 수 없습니다.");

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


}
