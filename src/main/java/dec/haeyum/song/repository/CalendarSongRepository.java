package dec.haeyum.song.repository;

import dec.haeyum.song.entity.CalendarSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarSongRepository extends JpaRepository<CalendarSong, Long> {
    List<CalendarSong> findByCalendarEntity_CalenderId(Long calenderId);

    @Query("SELECT cs FROM CalendarSong cs WHERE cs.calendarEntity.calenderId = :calendarId AND cs.ranking BETWEEN 1 AND 5")
    List<CalendarSong> findTop5ByCalenderEntityId(@Param("calendarId") Long calenderId);
}
