package dec.haeyum.song.entity;

import dec.haeyum.searchCalender.entity.CalenderEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


@Getter
@Entity
public class CalendarSong {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private CalenderEntity calenderEntity;
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    private int ranking;

}
