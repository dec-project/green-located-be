package dec.haeyum.song.controller;

import dec.haeyum.song.dto.SongDetailDto;
import dec.haeyum.song.dto.SongSummaryDto;
import dec.haeyum.song.service.SongService;
import dec.haeyum.song.service.impl.SongServiceImlp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/search/{calendarId}/songs")
    public ResponseEntity<List<SongSummaryDto>> getTop5Songs(@PathVariable Long calendarId) {
        List<SongSummaryDto> top5Songs = songService.getTop5Songs(calendarId);
        return ResponseEntity.ok(top5Songs);
    }

    @GetMapping("/search/{calendarId}/songs/{songId}")
    public ResponseEntity<SongDetailDto> getSongDetail(@PathVariable Long calendarId, @PathVariable Long songId) {
        SongDetailDto songDetails = songService.getSongDetails(songId);
        return ResponseEntity.ok(songDetails);
    }
}
