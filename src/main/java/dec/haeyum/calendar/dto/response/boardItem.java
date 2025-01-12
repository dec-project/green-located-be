package dec.haeyum.calendar.dto.response;

import dec.haeyum.calendar.entity.CalendarEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class boardItem {

    private Long CalendarId;
    private String img;
    private String calendarName;
    private Integer viewCount ;
    private Integer favoriteCount;


//    public static List<boardItem> asList(Page<CalendarEntity> paging) {
//        List<boardItem> list = new ArrayList<>();
//        for (CalendarEntity calendarEntity : paging.getContent()) {
//            boardItem boardItem = new boardItem();
//            boardItem.setCalendarId(calendarEntity.getCalendarId());
//            boardItem.setCalendarName(calendarEntity.getCalendarName());
//            boardItem.setViewCount(calendarEntity.getViewCount());
//            boardItem.setFavoriteCount(calendarEntity.getFavorite().size());
//            list.add(boardItem);
//        }
//        return list;
//    }
//



}
