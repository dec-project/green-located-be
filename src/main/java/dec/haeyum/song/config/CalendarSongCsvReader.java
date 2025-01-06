package dec.haeyum.song.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CalendarSongCsvReader {

    @Value("${song.calendar-song.csv-path}")
    private String calendarSongCsv;


}
