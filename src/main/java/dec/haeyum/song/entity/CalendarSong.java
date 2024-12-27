package dec.haeyum.song.entity;

import dec.haeyum.searchCalender.entity.CalenderEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


@Getter
@Entity
public class CalendarSong {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private CalenderEntity calendarEntity;
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    private int ranking;

}
