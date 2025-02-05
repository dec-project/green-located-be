package dec.haeyum.song.service;

import dec.haeyum.song.dto.SongDetailDto;
import dec.haeyum.song.dto.SongSummaryDto;
import dec.haeyum.song.entity.Song;

import java.util.List;

public interface SongService {

    List<SongSummaryDto> getTop5Songs(Long calendarId);
    String getCalendarSongImageUrl(Long calendarId);
    SongDetailDto getSongDetails(Long songId);
    Boolean existedSong();
}
