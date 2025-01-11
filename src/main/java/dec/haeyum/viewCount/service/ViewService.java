package dec.haeyum.viewCount.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ViewService {


    ResponseEntity<Void> increaseView(Long calendarId, HttpServletRequest request);


}
