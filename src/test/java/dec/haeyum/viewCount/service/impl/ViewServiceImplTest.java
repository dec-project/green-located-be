package dec.haeyum.viewCount.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.redis.RedisService;
import dec.haeyum.viewCount.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ViewServiceImplTest {



    @Autowired
    private  ViewService viewService;
    @Autowired
    private  CalendarService calendarService;
    @Autowired
    private  CalendarRepository calendarRepository;
    @Autowired
    private  RedisService redisService;

    @BeforeEach
    void setUp(){

    }


    @Test
    void increaseView(){

        //GIVEN
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String[] userIps = new String[10];
        for (int i = 0; i < userIps.length; i++) {
            userIps[i] = "192.168.0."+i ;
        }
        Long calendarId = 20003L;
        CalendarEntity calendar = calendarService.getCalendar(calendarId);
        Integer beforeViewCount = calendar.getViewCount();
        //WHEN
        for (String userIp : userIps) {
            executorService.submit(() -> {
                HttpServletRequest mockRequest = mock(HttpServletRequest.class);
                when(mockRequest.getRemoteAddr()).thenReturn(userIp);

                for (int i = 0; i < 10; i++) {
                    viewService.increaseView(calendarId,mockRequest);
                }

            });
        }
        executorService.shutdown();
        //THEN
        CalendarEntity calendar1 = calendarService.getCalendar(calendarId);
        Integer afterViewCount = calendar1.getViewCount();

        assertThat(afterViewCount).isEqualTo(beforeViewCount + 10);


    }
}