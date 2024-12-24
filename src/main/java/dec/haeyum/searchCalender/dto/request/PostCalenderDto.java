package dec.haeyum.searchCalender.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCalenderDto {

    private String startDate;
    private String endDate;

    public PostCalenderDto(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
