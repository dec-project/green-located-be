package dec.haeyum.song.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Song {
    @Id
    private Long songId;
    private String title;
    @ElementCollection
    private List<String> artists;
    private LocalDate releaseDate;
    private String genre;
    private String albumTitle;
    private String lyrics;
    @ElementCollection
    private List<String> lyricists;
    @ElementCollection
    private List<String> composers;
    @ElementCollection
    private List<String> arrangers;
    private String songImg;
    private String youtubeAddr;
    @OneToMany(mappedBy = "song")
    private List<CalendarSong> calendarSongs = new ArrayList<>();
}
