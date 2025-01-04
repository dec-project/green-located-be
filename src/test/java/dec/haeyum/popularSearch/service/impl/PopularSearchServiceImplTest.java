package dec.haeyum.popularSearch.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.popularSearch.dto.PopularSearchDto;
import dec.haeyum.popularSearch.service.PopularSearchService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class PopularSearchServiceImplTest {

    @Autowired
    private PopularSearchService popularSearchService;

    @Autowired
    private CalendarRepository calendarRepository;

    private CalendarEntity createCalendarEntity(int viewCount, String date, String name) {
        CalendarEntity entity = new CalendarEntity();
        entity.setViewCount(viewCount);
        entity.setCalendarDate(LocalDate.parse(date));
        entity.setCalendarName(name);
        return entity;
    }

    @Test
    void getPopularSearch() {

        calendarRepository.save(createCalendarEntity(100, "2024-01-01", "2025년 1월 1일"));
        calendarRepository.save(createCalendarEntity(50, "2024-01-02", "2025년 1월 2일"));
        calendarRepository.save(createCalendarEntity(80, "2024-01-03", "2025년 1월 3일"));
        calendarRepository.save(createCalendarEntity(0, "2024-01-04", "2025년 1월 4일"));
        calendarRepository.save(createCalendarEntity(130, "2024-01-05", "2025년 1월 5일"));
        calendarRepository.save(createCalendarEntity(20, "2024-01-06", "2025년 1월 6일"));


        List<PopularSearchDto> result = popularSearchService.getPopularSearch();
        assertEquals(5, result.size());
        assertEquals("2025년 1월 5일", result.get(0).getCalendarName());
        assertEquals("2025년 1월 1일", result.get(1).getCalendarName());
        assertEquals("2025년 1월 3일", result.get(2).getCalendarName());
        assertEquals("2025년 1월 2일", result.get(3).getCalendarName());
        assertEquals("2025년 1월 6일", result.get(4).getCalendarName());


        log.info("Top1: {}", result.get(0).getViewCount());
        log.info("Top2: {}", result.get(1).getViewCount());
        log.info("Top3: {}", result.get(2).getViewCount());
        log.info("Top4: {}", result.get(3).getViewCount());
        log.info("Top5: {}", result.get(4).getViewCount());

    }

  
}