package dec.haeyum.viewCount.controller;

import dec.haeyum.popularSearch.service.PopularSearchService;
import dec.haeyum.viewCount.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ViewController {

    private final ViewService viewService;
    private final PopularSearchService popularSearchService;


    @Operation(summary = "조회수 증가 ", description = "사용자 IP 식별 후 레디스로 IP 조회, 하루에 한 IP당 특정 달력 1번 증가")
    @GetMapping("/view/{calendarId}")
    public ResponseEntity<Void> increaseView(@PathVariable(name = "calendarId")Long calendarId, HttpServletRequest request){
        log.info("ViewController::increaseView::start");
        popularSearchService.incrementDailySearch(calendarId);
        ResponseEntity<Void> result = viewService.increaseView(calendarId, request);
        log.info("ViewController::increaseView::finish");
        return result;

    }

}
