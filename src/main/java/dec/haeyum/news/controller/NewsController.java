package dec.haeyum.news.controller;

import dec.haeyum.news.dto.response.GetNewsResponseDto;
import dec.haeyum.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final  NewsService newsService;

    @GetMapping("/search/{calendarId}/news")
    public ResponseEntity<GetNewsResponseDto> getNews (@PathVariable(name = "calendarId") Long calendarId) throws MalformedURLException {
        ResponseEntity<GetNewsResponseDto> news = newsService.getNews(calendarId);
        return news;
    }

}