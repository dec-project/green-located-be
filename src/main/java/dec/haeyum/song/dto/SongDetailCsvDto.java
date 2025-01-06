package dec.haeyum.song.dto;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SongDetailCsvDto {
    private Long songId;
    private String title;
    private String imgName;
    private String artists;
    private String albumTitle;
    private String releaseDate;
    private String genre;
    private String lyrics;
    private String lyricists;
    private String composers;
    private String arrangers;

    public static List<String> getFieldNames() {
        Field[] fields = SongDetailCsvDto.class.getDeclaredFields();
        List<String> result = new ArrayList<>();
        for(Field field : fields) {
            result.add(field.getName());
        }
        return result;
    }
}
