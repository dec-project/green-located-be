package dec.haeyum.searchCalender.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostCalenderRequestDto {
    @NotNull(message = "ND")
    private LocalDate startDate;
    @NotNull(message = "ND")
    private LocalDate endDate;
    private Integer page;
    private Integer size;

}
