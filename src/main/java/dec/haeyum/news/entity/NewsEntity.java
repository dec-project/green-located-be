package dec.haeyum.news.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "news")
@Getter
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;
    private NewsCategory category;
    private String title;
    private String content;
    private String url;
    @ManyToOne()
    @JoinColumn(name = "calendar_id")
    private CalendarEntity calendar;

}
