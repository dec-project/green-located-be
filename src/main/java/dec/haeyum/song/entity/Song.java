package dec.haeyum.song.entity;

import dec.haeyum.external.youtube.dto.YoutubeDetailDto;
import dec.haeyum.song.dto.SongDetailCsvDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Song {
    @Id
    private Long songId;
    private String title;
    private String artists;
    private String releaseDate;
    private String genre;
    private String albumTitle;
    @Column(columnDefinition = "TEXT")
    private String lyrics;
    @Column(columnDefinition = "TEXT")
    private String lyricists;
    @Column(columnDefinition = "TEXT")
    private String composers;
    @Column(columnDefinition = "TEXT")
    private String arrangers;
    private String imgName;
    private String youtubeAddr;
    @OneToMany(mappedBy = "song")
    private List<CalendarSong> calendarSongs = new ArrayList<>();

    public static Song of(SongDetailCsvDto songDetailCsvDto) {
        return Song.builder()
                .songId(songDetailCsvDto.getSongId())
                .title(songDetailCsvDto.getTitle())
                .artists(songDetailCsvDto.getArtists())
                .releaseDate(songDetailCsvDto.getReleaseDate())
                .genre(songDetailCsvDto.getGenre())
                .albumTitle(songDetailCsvDto.getAlbumTitle())
                .lyrics(songDetailCsvDto.getLyrics())
                .lyricists(songDetailCsvDto.getLyricists())
                .composers(songDetailCsvDto.getComposers())
                .arrangers(songDetailCsvDto.getArrangers())
                .imgName(songDetailCsvDto.getImgName())
                .build();
    }

    public void setImg(YoutubeDetailDto youtubeDetailDto) {
        this.youtubeAddr = youtubeDetailDto.getYoutubeAddr();
    }
}
