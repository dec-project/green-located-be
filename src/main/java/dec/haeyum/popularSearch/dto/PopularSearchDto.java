package dec.haeyum.popularSearch.dto;

import dec.haeyum.calendar.entity.CalendarEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class PopularSearchDto {
    private Long calendarId;
    private LocalDate calendarDate;
    private String calendarName; // 2022년 10월 12일
    private Integer viewCount;
    private Integer favoriteCount;
    private String imgUrl;
    private Long chatroomId;

    public static PopularSearchDto toDto(CalendarEntity calendarEntity, String imgUrl, Long chatroomId) {
        return PopularSearchDto.builder()
                .calendarId(calendarEntity.getCalendarId())
                .calendarDate(calendarEntity.getCalendarDate())
                .calendarName(calendarEntity.getCalendarName())
                .viewCount(calendarEntity.getViewCount())
                .favoriteCount(calendarEntity.getFavorite().size())
                .imgUrl(imgUrl)
                .chatroomId(chatroomId)
                .build();
    }
}
