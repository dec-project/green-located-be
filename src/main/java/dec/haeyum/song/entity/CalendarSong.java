package dec.haeyum.song.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.song.dto.CalendarSongCsvDto;
import dec.haeyum.song.repository.SongRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CalendarSong {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@ManyToOne
    //@JoinColumn(name = "calendar_id")

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    private int ranking;

    public static CalendarSong of(CalendarSongCsvDto calendarSongCsvDto, SongRepository songRepository) {
        Song song = songRepository.findById(calendarSongCsvDto.getSongId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SONG_NOT_FOUND));
        LocalDate startDate = LocalDate.parse(calendarSongCsvDto.getStartDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        LocalDate endDate = LocalDate.parse(calendarSongCsvDto.getEndDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        return CalendarSong.builder()
                .startDate(startDate)
                .endDate(endDate)
                .song(song)
                .ranking(calendarSongCsvDto.getRanking())
                .build();
    }

}
