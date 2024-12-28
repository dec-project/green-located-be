package dec.haeyum.calendar.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostCalendarRequestDto {
    @NotNull(message = "ND")
    private LocalDate startDate;
    @NotNull(message = "ND")
    private LocalDate endDate;
    private Integer page;
    private Integer size;

}
