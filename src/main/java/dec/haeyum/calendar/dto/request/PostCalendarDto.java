package dec.haeyum.calendar.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCalendarDto {

    private String startDate;
    private String endDate;

    public PostCalendarDto(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
