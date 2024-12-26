package dec.haeyum.weather.entity;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.weather.dto.response.WeatherApiResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weatherId;
    private String weatherName;
    private String stnNm; // 관측 지점
    private Double avgTa; // 평균 기온
    private Double sumRnDur; // 강수 지속시간
    private Double maxTaHrmt; // 최대 기온 시간
    private Double gr1MaxRn; // 1시간 최대 강수량
    private Double sumRn; //일 강수량(mm) , 비 또는 눈의 양
    private Double avgWs; // 평균 풍속(m/s)
    private Double sumSsHr; // 합계 일조 시간(hr), 태양이 비춘 시간
    private Double avgTca; // 평균 전운량 , 하늘의 평균 구름양
    private String iscs;
    @OneToOne(mappedBy = "weather")
    private CalendarEntity calendarEntity;

    public WeatherEntity(WeatherApiResponseDto dto) {
        this.stnNm = dto.getStnNm();
        this.avgTa = dto.getAvgTa();
        this.sumRnDur = dto.getSumRnDur();
        this.maxTaHrmt = dto.getMaxTaHrmt();
        this.gr1MaxRn = dto.getGr1MaxRn();
        this.sumRn = dto.getSumRn();
        this.avgWs = dto.getAvgWs();
        this.sumSsHr = dto.getSumSsHr();
        this.avgTca = dto.getAvgTca();
        this.iscs = transferString(dto.getIscs());
        this.weatherName = analysisWeather(dto.getIscs());
    }

    public void setCalendar(CalendarEntity calendar){
        this.calendarEntity = calendar;
    }

    private String transferString(Set<String> iscs) {
        return String.join(",", iscs.isEmpty() ? List.of("empty") : iscs);
    }

    private String analysisWeather(Set<String> iscs) {
        // 맑음, 흐림, 약간 흐림, 눈, 비, 뇌우
        if (iscs.contains("뇌우")) return "뇌우";
        if (iscs.contains("비")) return "비";
        if (iscs.contains("흐림")) return "흐림";
        if (iscs.contains("눈")) return "눈";
        if (iscs.contains("맑음")) return "맑음";


        if (this.sumRn > 0){
            return "비";
        }
        if (this.avgTca != null){
            if (this.avgTca > 4){
                return "흐림";
            }
        }
        return "맑음";
    }
}
