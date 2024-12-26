package dec.haeyum.calendar.entity;

import dec.haeyum.member.entity.MemberEntity;
import dec.haeyum.weather.entity.WeatherEntity;
import dec.haeyum.song.entity.CalendarSong;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "calendar")
@Getter

public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarId;
    private LocalDate calendarDate;
    private String calendarName; // 2022년 10월 12일
    private String content;
    private Integer viewCount;
    @ManyToMany(mappedBy = "favorite")
    private List<MemberEntity> favorite = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private WeatherEntity weather;


    public void createCalendar(LocalDate startDate) {
        // 2001-1-2 데이터를 2001년 1월 2일 로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String format = startDate.format(formatter);
        this.calendarName = format;
        this.calendarDate = startDate;
        this.viewCount = 0;
    }

    public void setWeather(WeatherEntity weather){
        this.weather = weather;
        this.weather.setCalendar(this);
    }
    @OneToMany(mappedBy = "calendarEntity")
    private List<CalendarSong> calendarSongs = new ArrayList<>();
}
