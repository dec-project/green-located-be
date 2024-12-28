package dec.haeyum.song.repository;

import dec.haeyum.song.entity.CalendarSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarSongRepository extends JpaRepository<CalendarSong, Long> {
    List<CalendarSong> findByCalendarEntity_CalendarId(Long calendarId);
}
