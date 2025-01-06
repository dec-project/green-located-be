package dec.haeyum.song.config;

import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.song.dto.CalendarSongCsvDto;
import dec.haeyum.song.entity.CalendarSong;
import dec.haeyum.song.entity.Song;
import dec.haeyum.song.entity.TemporaryCalendarSong;
import dec.haeyum.song.repository.CalendarSongRepository;
import dec.haeyum.song.repository.SongRepository;
import dec.haeyum.song.repository.TemporaryCalendarSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class CalendarSongCsvScheduleWriter implements ItemWriter<CalendarSongCsvDto> {

    private final TemporaryCalendarSongRepository temporaryCalendarSongRepository;

    @Override
    @Transactional(readOnly = true)
    public void write(Chunk<? extends CalendarSongCsvDto> chunk) throws Exception {
        Chunk<TemporaryCalendarSong> temporaryCalendarSongs = new Chunk<>();

        chunk.forEach(CalendarSongCsvDto -> {
            TemporaryCalendarSong temporaryCalendarSong = TemporaryCalendarSong.of(CalendarSongCsvDto);
            temporaryCalendarSongs.add(temporaryCalendarSong);
        });
        temporaryCalendarSongRepository.saveAll(temporaryCalendarSongs);
    }




}
