package dec.haeyum.song.dto;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CalendarSongCsvDto {
    private String calendarDate;
    private int ranking;
    private Long songId;

    public static List<String> getFieldNames() {
        Field[] fields = CalendarSongCsvDto.class.getDeclaredFields();
        List<String> result = new ArrayList<>();
        for(Field field : fields) {
            result.add(field.getName());
        }
        return result;
    }
}
