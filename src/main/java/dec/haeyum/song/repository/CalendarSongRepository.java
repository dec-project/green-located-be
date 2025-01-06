package dec.haeyum.song.repository;

import dec.haeyum.song.entity.CalendarSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CalendarSongRepository extends JpaRepository<CalendarSong, Long> {
/*
    @Query("SELECT cs FROM CalendarSong cs WHERE cs.calendarEntity.calendarId = :calendarId AND cs.ranking BETWEEN 1 AND 5")
    List<CalendarSong> findTop5ByCalendarEntityId(@Param("calendarId") Long calendarId);

 */

    @Query("SELECT s FROM CalendarSong s WHERE :date BETWEEN s.startDate AND s.endDate")
    List<CalendarSong> findSongsByDate(@Param("date") LocalDate date);
}
