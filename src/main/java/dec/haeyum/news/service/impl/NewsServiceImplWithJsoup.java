package dec.haeyum.news.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.news.dto.response.GetNewsResponseDto;
import dec.haeyum.news.dto.response.NewsItem;
import dec.haeyum.news.service.NewsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@Service("NewsServiceImplWithJsoup")
@Slf4j
@RequiredArgsConstructor
@Primary
public class NewsServiceImplWithJsoup implements NewsService {

    private final CalendarService calendarService;
    private WebClient webClient;
    @Value("${selenium.selenium-joongang-page}")
    private String news_api_url;

    @PostConstruct
    public void init(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(30 * 1024 * 1024))
                        .build())
                .build();
    }


    @Override
    public ResponseEntity<GetNewsResponseDto> getNews(Long calendarId) {

        ConcurrentLinkedQueue<NewsItem> itemList = new ConcurrentLinkedQueue<>();

        CalendarEntity calendar = calendarService.getCalendar(calendarId);
        String response = getDocument(calendar);
        Document document = Jsoup.parse(response);
        Elements select = document.select("li.card");
        Random random = new Random();

        try {

            // 2. 뉴스가 4개 미만일 경우
            if (select.size() < 4 && select.size() > 0){
                ExecutorService executorService = Executors.newFixedThreadPool(select.size());
                for (int i = 0; i < select.size(); i++) {
                    int num = i;
                    executorService.submit(() -> {
                        Element element = select.get(num);
                        crowlingPage(element,itemList);
                    });
                }
                shutdownExecutor(executorService);
            }

            // 1. 뉴스가 4개 이상일 경우
            if (select.size() >= 4){
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                Set<Integer> randomList = Collections.synchronizedSet(new HashSet<>());
                for (int i = 0; i < 4; i++) {
                    executorService.submit(() -> {
                        int randomIndex;
                        synchronized (randomList){
                            do {
                                randomIndex = random.nextInt(select.size());
                            }while (randomList.contains(randomIndex));
                            randomList.add(randomIndex);
                        }
                        Element element = select.get(randomIndex);
                        crowlingPage(element, itemList);
                    });
                }
                shutdownExecutor(executorService);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return GetNewsResponseDto.success(itemList);
    }

    private void shutdownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }



    private void crowlingPage(Element element, ConcurrentLinkedQueue<NewsItem> itemList) {
        String title = element.select("h2.headline a").text();
        String detailPath = element.select("h2.headline a").attr("href");
        NewsItem newsItem = new NewsItem(title,detailPath);
        crowlingDetailPage(detailPath,newsItem);
        itemList.add(newsItem);
    }

    private void crowlingDetailPage(String detailPath, NewsItem newsItem) {

        String response = webClient.get()
                .uri(detailPath)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Document document = Jsoup.parse(response);

        String content = document.select("div.article_body p[data-divno]").get(0).text();
        newsItem.setContent(content);

    }

    private String getDocument(CalendarEntity calendar) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String date = calendar.getCalendarDate().format(formatter);

        String response = webClient.get()
                .uri(news_api_url + date)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
}
