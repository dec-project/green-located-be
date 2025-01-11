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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/search/{calendarId}/songs")
    public ResponseEntity<Map<String, Object>> getTop5Songs(@PathVariable Long calendarId) {
        List<SongSummaryDto> top5Songs = songService.getTop5Songs(calendarId);

        Map<String, Object> response = new HashMap<>();
        response.put("songSummaries", top5Songs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{calendarId}/songs/{songId}")
    public ResponseEntity<SongDetailDto> getSongDetail(@PathVariable Long calendarId, @PathVariable Long songId) {
        SongDetailDto songDetails = songService.getSongDetails(songId);
        return ResponseEntity.ok(songDetails);
    }
}
