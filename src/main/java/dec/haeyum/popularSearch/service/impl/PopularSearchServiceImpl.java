package dec.haeyum.popularSearch.service.impl;

import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.popularSearch.dto.PopularSearchDto;
import dec.haeyum.popularSearch.service.PopularSearchService;
import dec.haeyum.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularSearchServiceImpl implements PopularSearchService {
    private final CalendarRepository calendarRepository;
    private final SongService songService;

    public List<PopularSearchDto> getPopularSearch() {
        Pageable pageable = PageRequest.of(0, 5);

        return calendarRepository.findTop5ByViewCount(pageable).stream()
                .map(calendarEntity -> {
                    String imgUrl = songService.getCalendarSongImageUrl(calendarEntity.getCalendarId());
                    return PopularSearchDto.toDto(calendarEntity, imgUrl);
                })
                .toList();
    }
}
