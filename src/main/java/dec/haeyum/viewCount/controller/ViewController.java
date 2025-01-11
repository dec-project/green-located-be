package dec.haeyum.viewCount.controller;

import dec.haeyum.viewCount.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ViewController {

    private final ViewService viewService;


    @Operation(summary = "조회수 증가 API", description = "사용자 IP 확인 후 하루에 한명의 사용자가 동일한 달력에 대해서 한번의 조회수 증가")
    @GetMapping("/view/{calendarId}")
    public ResponseEntity<Void> increaseView(@PathVariable(name = "calendarId")Long calendarId, HttpServletRequest request){
        viewService.increaseView(calendarId, request);
        return null;
    }

}
