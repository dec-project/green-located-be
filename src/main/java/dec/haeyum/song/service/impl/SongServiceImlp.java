package dec.haeyum.song.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SongServiceImlp implements SongService {

    private final SongRepository songRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarSongRepository calendarSongRepository;

    @Override
    @Transactional
    public List<SongSummaryDto> getTop5Songs(Long calendarId) {

        // 1. Top5 노래 조회
        //List<CalendarSong> calendarSongs = calendarSongRepository.findTop5ByCalendarEntityId(calendarId);
        CalendarEntity findCalendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR));
        List<CalendarSong> calendarSongs = calendarSongRepository.findSongsByDate(findCalendar.getCalendarDate());

        // 2. 각 Song의 이미지 url 생성
        List<SongSummaryDto> songSummaryDtos = calendarSongs.stream()
                .map(cs -> {
                    String imgName = cs.getSong().getImgName();
                    String imgUrl = "/img/song/" + imgName;

                    //DTO 변환
                    return SongSummaryDto.toDto(cs.getSong(), cs.getRanking(), imgUrl);
                })
                .toList();

        return songSummaryDtos;
    }

    @Override
    public SongDetailDto getSongDetails(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SONG_NOT_FOUND));

        return SongDetailDto.toDto(song);
    }
}
