package dec.haeyum.calendar.dto.response;

import dec.haeyum.calendar.entity.CalendarEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCalendarDto {

    private Long calendarId;
    private String img;
    private String calendarName; // 2022년 10월 12일
    private Integer viewCount;
    private Integer favoriteCount;

    public ResponseCalendarDto(CalendarEntity calendar, String calendarSongImageUrl) {
        this.calendarId = calendar.getCalendarId();
        this.calendarName = calendar.getCalendarName();
        this.viewCount = calendar.getViewCount();
        this.favoriteCount = calendar.getFavorite().size();
        this.img = calendarSongImageUrl;
    }
}
