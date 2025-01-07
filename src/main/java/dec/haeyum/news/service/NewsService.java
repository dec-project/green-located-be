package dec.haeyum.news.service;

import dec.haeyum.news.dto.response.GetNewsResponseDto;
import org.springframework.http.ResponseEntity;

public interface NewsService {
    ResponseEntity<GetNewsResponseDto> getNews(Long calendarId);
}
