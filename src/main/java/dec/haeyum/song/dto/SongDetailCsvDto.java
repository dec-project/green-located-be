package dec.haeyum.song.dto;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SongDetailCsvDto {
    private Long songId;
    private String title;
    private String artists;
    private LocalDate releaseDate;
    private String genre;
    private String albumTitle;
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
