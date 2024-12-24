package dec.haeyum.searchCalender.dto.response;

import dec.haeyum.searchCalender.entity.CalenderEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class boardItem {

    private Long CalenderId;
    private String img;
    private String calenderName;
    private Integer viewCount ;
    private Integer favoriteCount;

    public static List<boardItem> asList(Page<CalenderEntity> paging) {
        List<boardItem> list = new ArrayList<>();
        for (CalenderEntity calenderEntity : paging.getContent()) {
            boardItem boardItem = new boardItem();
            boardItem.setCalenderId(calenderEntity.getCalenderId());
            boardItem.setCalenderName(calenderEntity.getCalenderName());
            boardItem.setViewCount(calenderEntity.getViewCount());
            boardItem.setFavoriteCount(calenderEntity.getFavorite().size());
            list.add(boardItem);
        }
        return list;
    }
}
