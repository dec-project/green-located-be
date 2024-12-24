package dec.haeyum.searchCalender.dto.response;

import dec.haeyum.searchCalender.dto.ResponseCode;
import dec.haeyum.searchCalender.dto.ResponseDto;
import dec.haeyum.searchCalender.entity.CalenderEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostCalenderResponseDto extends ResponseDto {

    private Integer count;
    private Integer totalPages;
    private Integer currentPage;
    private boolean last;
    private List<boardItem> itemList = new ArrayList<>();

    public PostCalenderResponseDto() {
        super(ResponseCode.success,ResponseMessage.success);
    }

    public PostCalenderResponseDto(Page<CalenderEntity> paging) {
        super(ResponseCode.success,ResponseMessage.success);
        this.count = (int)paging.getTotalElements();
        this.totalPages = paging.getTotalPages() - 1;
        this.currentPage = paging.getNumber();
        this.last = paging.isLast();
        this.itemList = boardItem.asList(paging);
    }


    public static ResponseEntity<? super PostCalenderResponseDto> success(Page<CalenderEntity> paging) {
        PostCalenderResponseDto postCalenderResponseDto = new PostCalenderResponseDto(paging);
        return ResponseEntity.status(HttpStatus.OK).body(postCalenderResponseDto);
    }

    public static ResponseEntity<ResponseDto> notExistedBoundary() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.notExistedBoundary, ResponseMessage.notExistedBoundary);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> notExistedCalender() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.notExistedData, ResponseMessage.notExistedData);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
