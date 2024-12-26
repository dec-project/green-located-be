package dec.haeyum.song.service.impl;

import dec.haeyum.song.dto.SongDetailDto;
import dec.haeyum.song.dto.SongSummaryDto;
import dec.haeyum.song.entity.CalendarSong;
import dec.haeyum.song.entity.Song;
import dec.haeyum.song.repository.CalendarSongRepository;
import dec.haeyum.song.repository.SongRepository;
import dec.haeyum.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SongServiceImlp implements SongService {

    private final SongRepository songRepository;
    private final CalendarSongRepository calendarSongRepository;

    @Override
    @Transactional
    public List<SongSummaryDto> getTop5Songs(Long calendarId) {
        List<CalendarSong> calendarSongs = calendarSongRepository.findByCalenderEntity_CalenderId(calendarId);
        List<SongSummaryDto> songSummaryDtos = calendarSongs.stream()
                .map(cs -> SongSummaryDto.toDto(cs.getSong(), cs.getRanking()))
                .toList();

        return songSummaryDtos;
    }

    @Override
    public SongDetailDto getSongDetails(Long songId) {
        return null;
    }
}
