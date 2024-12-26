package dec.haeyum.calendar.dto.response;

import dec.haeyum.calendar.dto.ResponseCode;
import dec.haeyum.calendar.dto.ResponseDto;
import dec.haeyum.calendar.entity.CalendarEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostCalendarResponseDto extends ResponseDto {

    private Integer count;
    private Integer totalPages;
    private Integer currentPage;
    private boolean last;
    private List<boardItem> itemList = new ArrayList<>();

    public PostCalendarResponseDto() {
        super(ResponseCode.success,ResponseMessage.success);
    }

    public PostCalendarResponseDto(Page<CalendarEntity> paging) {
        super(ResponseCode.success,ResponseMessage.success);
        this.count = (int)paging.getTotalElements();
        this.totalPages = paging.getTotalPages() - 1;
        this.currentPage = paging.getNumber();
        this.last = paging.isLast();
        this.itemList = boardItem.asList(paging);
    }


    public static ResponseEntity<? super PostCalendarResponseDto> success(Page<CalendarEntity> paging) {
        PostCalendarResponseDto postCalendarResponseDto = new PostCalendarResponseDto(paging);
        return ResponseEntity.status(HttpStatus.OK).body(postCalendarResponseDto);
    }

    public static ResponseEntity<ResponseDto> notExistedBoundary() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.notExistedBoundary, ResponseMessage.notExistedBoundary);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> notExistedCalendar() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.notExistedData, ResponseMessage.notExistedData);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
