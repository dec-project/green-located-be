package dec.haeyum.calendar.dto.response;

import dec.haeyum.calendar.entity.CalendarEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseCalendarDto {

    private Long calendarId;
    private String imgUrl;
    private String calendarName; // 2022년 10월 12일
    private LocalDate calendarDate;
    private Integer viewCount;
    private Integer favoriteCount;
    private Long chatroomId;

    public ResponseCalendarDto(CalendarEntity calendar, String calendarSongImageUrl, Long chatroomId) {
        this.calendarId = calendar.getCalendarId();
        this.calendarName = calendar.getCalendarName();
        this.viewCount = calendar.getViewCount();
        this.favoriteCount = calendar.getFavorite().size();
        this.imgUrl = calendarSongImageUrl;
        this.chatroomId = chatroomId;
        this.calendarDate = calendar.getCalendarDate();
    }
}
