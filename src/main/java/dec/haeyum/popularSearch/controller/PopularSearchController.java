package dec.haeyum.popularSearch.controller;

import dec.haeyum.popularSearch.dto.PopularSearchDto;
import dec.haeyum.popularSearch.service.PopularSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PopularSearchController {

    private final PopularSearchService popularSearchService;

    @GetMapping("/ranking/search")
    public ResponseEntity<List<PopularSearchDto>> getPopularSearch() {
        return ResponseEntity.ok(popularSearchService.getPopularSearch());
    }
}
