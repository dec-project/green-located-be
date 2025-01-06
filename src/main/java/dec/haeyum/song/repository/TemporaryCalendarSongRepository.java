package dec.haeyum.song.repository;

import dec.haeyum.song.entity.TemporaryCalendarSong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryCalendarSongRepository extends JpaRepository<TemporaryCalendarSong, Long> {
}
