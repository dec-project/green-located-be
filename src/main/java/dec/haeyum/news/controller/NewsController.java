package dec.haeyum.news.controller;

import dec.haeyum.news.dto.response.GetNewsResponseDto;
import dec.haeyum.news.service.NewsService;
import dec.haeyum.news.service.impl.NewsServiceImplWithJsoup;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class NewsController {

    private  NewsService newsService;

    public NewsController(@Qualifier("NewsServiceImplWithJsoup")NewsService newsService) {
        this.newsService = newsService;
    }
    @Operation(summary = "뉴스 조회", description = "요청 달력 일자의 뉴스 크롤링 후 4개의 뉴스 반환")
    @GetMapping("/search/{calendarId}/news")
    public ResponseEntity<GetNewsResponseDto> getNews (@PathVariable(name = "calendarId") Long calendarId) throws MalformedURLException {
        ResponseEntity<GetNewsResponseDto> news = newsService.getNews(calendarId);
        return news;
    }

}