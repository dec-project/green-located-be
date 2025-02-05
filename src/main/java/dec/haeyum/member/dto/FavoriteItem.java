package dec.haeyum.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FavoriteItem {

    private Long calendarId;
    private String img;
    private String calendarName;
    private Long chatroomId;
    private LocalDate calendarDate;
    private Boolean isFavorite;

    public FavoriteItem(Long calendarId, String calendarName, LocalDate calendarDate, Long chatRoomIdByCalendar, String calendarSongImageUrl) {
        this.calendarId = calendarId;
        this.img = calendarSongImageUrl;
        this.calendarName = calendarName;
        this.chatroomId = chatRoomIdByCalendar;
        this.calendarDate = calendarDate;
        this.isFavorite = true;
    }

}
