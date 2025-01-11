package dec.haeyum.calendar.dto.response;

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
public class PostCalendarResponseDto {

    private Integer count;
    private Integer totalPages;
    private Integer currentPage;
    private boolean last;
    private List<ResponseCalendarDto> itemList = new ArrayList<>();

    public PostCalendarResponseDto(Page<CalendarEntity> paging, List<ResponseCalendarDto> responseItems) {
        this.count = (int)paging.getTotalElements();
        this.totalPages = paging.getTotalPages() - 1;
        this.currentPage = paging.getNumber();
        this.last = paging.isLast();
        this.itemList.addAll(responseItems);
    }

    public static ResponseEntity<PostCalendarResponseDto> success(Page<CalendarEntity> paging, List<ResponseCalendarDto> responseItems) {
        PostCalendarResponseDto result = new PostCalendarResponseDto(paging, responseItems);
        return ResponseEntity.ok(result);
    }

//
//    public PostCalendarResponseDto(Page<CalendarEntity> paging) {
//        this.count = (int)paging.getTotalElements();
//        this.totalPages = paging.getTotalPages() - 1;
//        this.currentPage = paging.getNumber();
//        this.last = paging.isLast();
//        this.itemList = boardItem.asList(paging);
//    }



//    public static ResponseEntity<? super PostCalendarResponseDto> success(Page<CalendarEntity> paging) {
//        PostCalendarResponseDto postCalendarResponseDto = new PostCalendarResponseDto(paging);
//        return ResponseEntity.status(HttpStatus.OK).body(postCalendarResponseDto);
//    }


}
