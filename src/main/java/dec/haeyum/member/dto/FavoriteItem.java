package dec.haeyum.member.dto;

import dec.haeyum.calendar.entity.CalendarEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FavoriteItem {

    private Long calendarId;
    private String img;
    private String calendarName;
    private Boolean isFavorite;
    public FavoriteItem(Long calendarId, String calendarName, String calendarSongImageUrl) {
        this.calendarId = calendarId;
        this.img = calendarSongImageUrl;
        this.calendarName = calendarName;
        this.isFavorite = true;
    }

}
