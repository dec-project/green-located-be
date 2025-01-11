package dec.haeyum.news.service;

import dec.haeyum.news.dto.response.GetNewsResponseDto;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;

public interface NewsService {
    ResponseEntity<GetNewsResponseDto> getNews(Long calendarId) throws MalformedURLException;
}
