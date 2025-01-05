package dec.haeyum.song.controller;

import dec.haeyum.song.service.SongDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SongDataController {

    private final SongDataService songDataService;

}
