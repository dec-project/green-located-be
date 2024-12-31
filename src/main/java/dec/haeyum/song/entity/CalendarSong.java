package dec.haeyum.song.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class CalendarSong {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "calendar_id")

    private CalendarEntity calendarEntity;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    private int ranking;

}
