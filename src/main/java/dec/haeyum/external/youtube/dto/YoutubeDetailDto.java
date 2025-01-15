package dec.haeyum.external.youtube.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Slf4j
public class YoutubeDetailDto {

    private String youtubeAddr;
    private LocalDate releaseDate;


    public void setData(String videoId, String releaseDate) {
        this.youtubeAddr = "https://www.youtube.com/embed/" + videoId;

        DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;
        ZonedDateTime zonDate = ZonedDateTime.parse(releaseDate, isoDateTime);
        LocalDate localDate = zonDate.toLocalDate();
        this.releaseDate = localDate;

        log.info("youtubeAddr={} , releaseDate={}",youtubeAddr,this.releaseDate);

    }
}
