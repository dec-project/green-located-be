package dec.haeyum.news.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.news.dto.response.GetNewsResponseDto;
import dec.haeyum.news.dto.response.NewsItem;
import dec.haeyum.news.service.NewsService;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final CalendarService calendarService;

    @Value("${selenium.selenium-driver-name}")
    private String seleniumName;
    @Value("${selenium.selenium-driver-path}")
    private String seleniumPath;


    @Value("${selenium.selenium-bigKinds-page}")
    private String big_kinds_page;
    @Value("${selenium.selenium-joongang-page}")
    private String joongAng_page;
    @Value("${selenium.isHeadless}")
    private boolean isHeadLess;


//    @Override
//    @Transactional
//    public ResponseEntity<GetNewsResponseDto> getNews(Long calendarId) {
//        CalendarEntity calendar = calendarRepository.findById(calendarId).orElse(null);
//
//        System.out.println("셀레니움 시작");
//
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        options.addArguments("--no-sandbox");
////        options.addArguments("--disable-dev-shm-usage");
////        options.addArguments("--disable-gpu");
////        options.addArguments("--single-process");
////        options.addArguments("--verbose");
//
//        ChromeDriver driver = new ChromeDriver(options);
//
//        System.out.println("셀레니움 세팅 완료");
//
//        if (calendar == null){
//            throw new BusinessException(ErrorCode.NOT_EXISTED_CALENDAR);
//        }
//
//        List<NewsItem> itemList = new ArrayList<>();
//
//        LocalDate current = LocalDate.of(1990,01,01);
//
//        try {
//
//            // 1. 1990년 1월 1일 이전 자료
//            if (calendar.getCalendarDate().isBefore(current)){
//                String urlDate = calendar.getCalendarDate().toString().replace("-", "/");
//                driver.get(joongAng_page+urlDate);
//                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
//
//                searchGeneral2(itemList, homePage, driver);
//
//            }
//
//            // 2. 1990년 1월 1일 이후 자료
//            if (calendar.getCalendarDate().isEqual(current) || calendar.getCalendarDate().isAfter(current)){
//
//                driver.get(big_kinds_page);
//                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
//                searchGeneral(calendar,itemList, homePage, driver);
//                driver.get(big_kinds_page);
//                searchIt(calendar,itemList, homePage, driver);
//                driver.get(big_kinds_page);
//                searchEntertainment(calendar, itemList, homePage, driver);
//                driver.get(big_kinds_page);
//                searchSports(calendar,itemList, homePage, driver);
//            }
//
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }finally {
//            driver.close();
//        }
//
//        return GetNewsResponseDto.success(itemList);
//    }



    @Override
    public ResponseEntity<GetNewsResponseDto> getNews(Long calendarId) {
        log.info("NewsServiceImpl::getNews::start");
        CalendarEntity calendar = calendarService.getCalendar(calendarId);
        log.info("calendarDate={}",calendar.getCalendarDate());
        List<NewsItem> itemList = Collections.synchronizedList(new ArrayList<>());


        LocalDate current = LocalDate.of(1990, 1, 1);

        try {
            // 1. 1990년 1월 1일 이전 자료
            if (calendar.getCalendarDate().isBefore(current)){
                ChromeDriver driver = createDriver();
                String urlDate = calendar.getCalendarDate().toString().replace("-", "/");
                driver.get(joongAng_page+urlDate);
                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
                searchGeneral2(itemList, homePage, driver);
            }else {
                ExecutorService executorService = Executors.newFixedThreadPool(8);

                searchNewsData(executorService, calendar, itemList);
                log.info("itemList={}",itemList.toString());

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return GetNewsResponseDto.success(itemList);
    }

    private void searchNewsData(ExecutorService executorService, CalendarEntity calendar, List<NewsItem> itemList) throws InterruptedException {
        log.info("searchnewsData::start");
        int taskCount = 4;
        CountDownLatch countDownLatch = new CountDownLatch(taskCount);
        executorService.submit(() -> {
            try {
                ChromeDriver driver = createDriver();
                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
                driver.get(big_kinds_page);
                log.info("driver.get={}",driver.getTitle());
                searchGeneral(calendar, itemList, homePage, driver);
                driver.quit();
                executorService.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                ChromeDriver driver = createDriver();
                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
                driver.get(big_kinds_page);
                searchIt(calendar, itemList, homePage, driver);
                driver.quit();
                executorService.shutdown();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });

        executorService.submit(() -> {
            try {
                ChromeDriver driver = createDriver();
                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
                driver.get(big_kinds_page);
                searchEntertainment(calendar, itemList, homePage, driver);
                driver.quit();
                executorService.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });

        executorService.submit(() -> {
            try {
                ChromeDriver driver = createDriver();
                WebDriverWait homePage = new WebDriverWait(driver, Duration.ofSeconds(5));
                driver.get(big_kinds_page);
                searchSports(calendar, itemList, homePage, driver);
                driver.quit();
                executorService.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        // 스레드 종료 대기
    }

    private ChromeDriver createDriver() {
        ChromeOptions options = new ChromeOptions();


        if (!isHeadLess){
            WebDriverManager.chromedriver().setup();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-images");
            options.addArguments("--blink-settings=imagesEnabled=false");
            options.addArguments("--disable-javascript");
            ChromeDriver driver = new ChromeDriver(options);

        }

        if (isHeadLess){
            WebDriverManager.chromedriver().setup();
            //System.setProperty(seleniumName,seleniumPath);
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-images");
            options.addArguments("--blink-settings=imagesEnabled=false");
            options.addArguments("--disable-javascript");
        }
        ChromeDriver driver = new ChromeDriver(options);
        return driver;

    }


    private void searchGeneral2(List<NewsItem> itemList, WebDriverWait homePage, ChromeDriver driver) {

        String category = "종합";

        List<WebElement> newsItemList = homePage.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.card")));
        if (newsItemList.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_NEWS);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(newsItemList.size());
        WebElement targetNewsItem = newsItemList.get(randomIndex);
        WebElement element = targetNewsItem.findElement(By.cssSelector("a"));
        String newsUrl = element.getAttribute("href");
        driver.get(newsUrl);
        WebDriverWait newsDetailPage = new WebDriverWait(driver, Duration.ofSeconds(5));
        String title = newsDetailPage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("h1.headline"))).getText();
        String content = newsDetailPage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.article_body p"))).getText();

        String substringContent = content.length() > 100 ? content.substring(0,100) : content;
        NewsItem newsItem = new NewsItem(title,  newsUrl,substringContent, category);
        itemList.add(newsItem);


    }

    private void searchGeneral(CalendarEntity calendar, List<NewsItem> itemList, WebDriverWait homePage, ChromeDriver driver) throws InterruptedException {

        String categoryName = "종합";

        selecetDate(calendar, homePage, driver);

        selectGeneralCategory(homePage, driver);

        dataMapping(itemList, homePage,categoryName, driver);


    }

    private void selectGeneralCategory(WebDriverWait homePage, ChromeDriver driver) throws InterruptedException {
        WebElement category = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.tab3")));
        category.click();
        WebElement button = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div#search-foot-div div.foot-btn button.news-report-search-btn")));
        button.click();
    }

    private void searchIt(CalendarEntity calendar, List<NewsItem> itemList, WebDriverWait homePage, ChromeDriver driver) throws InterruptedException {

        String categoryName = "IT";

        selecetDate(calendar, homePage, driver);

        selectItCategory(homePage);

        dataMapping(itemList, homePage,categoryName, driver);


    }

    private void selectItCategory(WebDriverWait homePage) throws InterruptedException {
        WebElement category = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.tab3")));
        category.click();
        WebElement categoryDetail = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-id='008000000'] div[data-role='wrapper'] span[data-role='display']")));
        categoryDetail.click();
        WebElement button = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div#search-foot-div div.foot-btn button.news-report-search-btn")));
        button.click();
    }

    private void searchSports(CalendarEntity calendar, List<NewsItem> itemList, WebDriverWait homePage, ChromeDriver driver) throws InterruptedException {

        String categoryName = "스포츠";

        selecetDate(calendar, homePage, driver);

        selectSportsCategory(homePage);

        dataMapping(itemList, homePage, categoryName, driver);

    }

    private void selectSportsCategory(WebDriverWait homePage) throws InterruptedException {
        WebElement category = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.tab3")));
        category.click();
        WebElement categoryDetail = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-id='007000000'] div[data-role='wrapper'] span[data-role='display']")));
        categoryDetail.click();
        WebElement button = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div#search-foot-div div.foot-btn button.news-report-search-btn")));
        button.click();
    }


    private  void searchEntertainment(CalendarEntity calendar, List<NewsItem> itemList, WebDriverWait homePage, ChromeDriver driver) throws InterruptedException {

        String categoryName = "연예";

        selecetDate(calendar, homePage, driver);

        selectEntertainmentCategory(homePage);

        dataMapping(itemList, homePage,categoryName, driver);

    }

    private static void dataMapping(List<NewsItem> itemList, WebDriverWait homePage, String categoryName, ChromeDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> newsItemList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.news-item")));
        if (newsItemList.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTED_NEWS);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(newsItemList.size());
        WebElement targetNewsItem = newsItemList.get(randomIndex);
        targetNewsItem.click();

        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement detailPageTitle = wait1.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.news-view-head h1.title")));
        log.info("detailPageTitle={}",detailPageTitle.toString());
        // 뉴스 제목
        String title = detailPageTitle.getText();
        String originalNewsPageValue = null;

        if (!driver.findElements(By.cssSelector("div.news-view-head div.item2 div button[onclick*='location.href']")).isEmpty()){
            WebElement originalNewsPage = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.news-view-head div.item2 div button[onclick*='location.href']")));
            String originalNewsPageButton = originalNewsPage.getAttribute("onclick");
            // 뉴스 원문 주소
            originalNewsPageValue = originalNewsPageButton.split("'")[3];
        }


        WebElement newsBody = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.news-view-body")));
        String text = newsBody.getText();
        String substringText = text.length() > 100 ? text.substring(0, 100) : text;

        NewsItem newsItem = new NewsItem(title, originalNewsPageValue, substringText, categoryName);
        log.info("newsItem={}",newsItem.toString());
        itemList.add(newsItem);
    }

    private static void selectEntertainmentCategory(WebDriverWait homePage) throws InterruptedException {
        WebElement category = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.tab3")));
        category.click();
        WebElement categoryDetail = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-id='004000000'] div[data-role='wrapper'] span.fal")));
        categoryDetail.click();
        WebElement categoryDetailSelect = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-id='004007000'] div[data-role='wrapper'] span[data-role='display']")));
        categoryDetailSelect.click();
        WebElement button = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div#search-foot-div div.foot-btn button.news-report-search-btn")));
        button.click();
    }

    private static void selecetDate(CalendarEntity calendar, WebDriverWait homePage, ChromeDriver driver) {
        WebElement period = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.tab1")));
        period.click();
        WebElement date = homePage.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='date1-7']")));
        date.click();
        WebElement dateInput = homePage.until(ExpectedConditions.elementToBeClickable(By.id("search-begin-date")));
        JavascriptExecutor js = driver;
        js.executeScript("document.getElementById('search-begin-date').value='" + String.valueOf(calendar.getCalendarDate()) + "';");
        js.executeScript("document.getElementById('search-end-date').value='" + String.valueOf(calendar.getCalendarDate()) + "';");
    }

}
