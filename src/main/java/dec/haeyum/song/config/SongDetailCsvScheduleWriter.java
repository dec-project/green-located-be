package dec.haeyum.song.config;

import dec.haeyum.song.dto.SongDetailCsvDto;
import dec.haeyum.song.entity.Song;
import dec.haeyum.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class SongDetailCsvScheduleWriter implements ItemWriter<SongDetailCsvDto> {

    private final SongRepository songRepository;

    @Override
    @Transactional(readOnly = true)
    public void write(Chunk<? extends SongDetailCsvDto> chunk) throws Exception {
        Chunk<Song> songs = new Chunk<>();

        chunk.forEach(songDetailCsvDto -> {
            Song song = Song.of(songDetailCsvDto);
            songs.add(song);
        });
        songRepository.saveAll(songs);
    }
}
