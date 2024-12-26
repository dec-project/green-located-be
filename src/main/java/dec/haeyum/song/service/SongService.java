package dec.haeyum.song.service;

import dec.haeyum.song.dto.SongDetailDto;
import dec.haeyum.song.dto.SongSummaryDto;

import java.util.List;

public interface SongService {

    List<SongSummaryDto> getTop5Songs(Long calendarId);
    SongDetailDto getSongDetails(Long songId);
}
