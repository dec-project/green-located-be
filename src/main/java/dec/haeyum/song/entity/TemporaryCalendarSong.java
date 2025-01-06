package dec.haeyum.song.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.song.dto.CalendarSongCsvDto;
import dec.haeyum.song.repository.SongRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TemporaryCalendarSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate calendarDate;
    private int ranking;
    private Long songId;

    public static TemporaryCalendarSong of(CalendarSongCsvDto calendarSongCsvDto) {
        LocalDate calendarDate = LocalDate.parse(calendarSongCsvDto.getCalendarDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return TemporaryCalendarSong.builder()
                .calendarDate(calendarDate)
                .songId(calendarSongCsvDto.getSongId())
                .ranking(calendarSongCsvDto.getRanking())
                .build();
    }
}
