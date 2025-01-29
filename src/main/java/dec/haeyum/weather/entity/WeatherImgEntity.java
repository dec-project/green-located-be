package dec.haeyum.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "weather_img")
@Getter
@Setter
@NoArgsConstructor
public class WeatherImgEntity {
    @Override
    public String toString() {
        return "WeatherImgEntity{" +
                "weatherImgId=" + weatherImgId +
                ", weatherImgName='" + weatherImgName + '\'' +
                ", weatherImg='" + weatherImg + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weatherImgId;
    private String weatherImgName;
    private String weatherImg;

    public WeatherImgEntity(String weatherImgName, String uuidFileName) {
        this.weatherImgName = weatherImgName;
        this.weatherImg = uuidFileName;
    }

    public void create(String weatherImgName, String uuidFileName) {
        this.weatherImgName = weatherImgName;
        this.weatherImg = weatherImg;
    }
}
