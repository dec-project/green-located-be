package dec.haeyum.song.config;

import dec.haeyum.song.dto.CalendarSongCsvDto;
import dec.haeyum.song.entity.CalendarSong;
import dec.haeyum.song.repository.CalendarSongRepository;
import dec.haeyum.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class CalendarSongCsvScheduleWriter implements ItemWriter<CalendarSongCsvDto> {

    private final CalendarSongRepository calendarSongRepository;
    private final SongRepository songRepository;

    @Override
    @Transactional(readOnly = true)
    public void write(Chunk<? extends CalendarSongCsvDto> chunk) throws Exception {
        Chunk<CalendarSong> calendarSongs = new Chunk<>();

        chunk.forEach(calendarSongCsvDto -> {
            CalendarSong calendarSong = CalendarSong.of(calendarSongCsvDto, songRepository);
            calendarSongs.add(calendarSong);
        });
        calendarSongRepository.saveAll(calendarSongs);
    }




}
