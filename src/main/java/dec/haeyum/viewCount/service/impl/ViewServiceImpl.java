package dec.haeyum.viewCount.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.service.CalendarService;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.redis.RedisService;
import dec.haeyum.viewCount.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {

    private final CalendarService calendarService;
    private final RedisService redisService;


    @Override
    @Transactional
    public ResponseEntity<Void> increaseView(Long calendarId, HttpServletRequest request) {
        // HashMap<LocalDate, HashMap<LocalDate,List<String>>> list
        // HashMap<CalendarDate, HashMap<currentDate, List<userIP>>>
        // redis -> key : {calendarDate::currentDate} , value : {userIP}

        CalendarEntity calendar = calendarService.getCalendar(calendarId);
        LocalDate currentDate = LocalDate.now();
        // 2010-12-23::2024-12-24
        String redisKey = calendar.getCalendarDate()+ "::" + currentDate;
        String userIp = getUserIp(request);
        Boolean isValue = redisService.isValueInSet(redisKey, userIp);

        if (!isValue){
            redisService.setValuesInSet(redisKey,userIp);
            calendarService.increaseViewCount(calendar);
        }
        return ResponseEntity.ok().build();
    }





    private String getUserIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }


}
