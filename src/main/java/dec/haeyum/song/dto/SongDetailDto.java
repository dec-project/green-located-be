package dec.haeyum.song.dto;

import dec.haeyum.song.entity.Song;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public class SongDetailDto {
    private Long songId;
    private String title;
    private String artists;
    private String releaseDate;
    private String genre;
    private String albumTitle;
    private String lyrics;
    private String lyricists;
    private String composers;
    private String arrangers;
    private String youtubeAddr;

    public static SongDetailDto toDto(Song song) {
        return SongDetailDto.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .artists(song.getArtists())
                .releaseDate(song.getReleaseDate())
                .genre(song.getGenre())
                .albumTitle(song.getAlbumTitle())
                .lyrics(song.getLyrics())
                .lyricists(song.getLyricists())
                .composers(song.getComposers())
                .arrangers(song.getArrangers())
                .youtubeAddr(song.getYoutubeAddr())
                .build();
    }
}
