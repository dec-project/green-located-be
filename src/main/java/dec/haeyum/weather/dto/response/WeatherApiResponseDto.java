package dec.haeyum.weather.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class WeatherApiResponseDto {


    private String stnNm; // 관측 지점
    private Double avgTa; // 평균 기온
    private Double sumRnDur; // 강수 지속시간
    private Double maxTaHrmt; // 최대 기온 시간
    private Double gr1MaxRn; // 1시간 최대 강수량
    private Double sumRn; //일 강수량(mm) , 비 또는 눈의 양
    private Double avgWs; // 평균 풍속(m/s)
    private Double sumSsHr; // 합계 일조 시간(hr), 태양이 비춘 시간
    private Double avgTca; // 평균 전운량 , 하늘의 평균 구름양
    private Set<String> iscs = new LinkedHashSet<>();


    public void setData(JsonNode dto) {
        JsonNode result = dto.get(0);
        this.stnNm = result.path("stnNm").asText();
        this.avgTa = result.path("avgTa").asDouble();
        this.sumRnDur = result.path("sumRnDur").asDouble();
        this.maxTaHrmt = result.path("maxTaHrmt").asDouble();
        this.gr1MaxRn = result.path("gr1MaxRn").asDouble();
        this.sumRn = result.path("sumRn").asDouble();
        this.avgWs = result.path("avgWs").asDouble();
        this.sumSsHr = result.path("sumSsHr").asDouble();
        this.avgTca = result.path("avgTca").asDouble();

        Pattern compile = Pattern.compile("\\{([^}]*)\\}");
        Matcher matcher = compile.matcher(result.path("iscs").asText());
        while (matcher.find()){
            String word = matcher.group(1);
            if (word.contains("비") || word.contains("소나기")){
                iscs.add("비");
            } else if (word.contains("눈")) {
                iscs.add("눈");
            }else if (word.contains("뇌전") || word.contains("번개")){
                iscs.add("뇌우");
            } else if (word.contains("맑음")) {
                iscs.add("맑음");
            } else if (word.contains("흐림")) {
                iscs.add("흐림");

            }
        }
    }
}
