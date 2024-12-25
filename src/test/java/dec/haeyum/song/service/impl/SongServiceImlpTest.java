package dec.haeyum.song.service.impl;

import dec.haeyum.searchCalender.entity.CalenderEntity;
import dec.haeyum.song.dto.SongSummaryDto;
import dec.haeyum.song.entity.CalendarSong;
import dec.haeyum.song.entity.Song;
import dec.haeyum.song.repository.CalendarSongRepository;
import dec.haeyum.song.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SongServiceImlpTest {

    @Autowired
    private SongService songService;

    @Autowired
    private CalendarSongRepository calendarSongRepository;

    @Test
    void getTop5Songs() {

    }

}