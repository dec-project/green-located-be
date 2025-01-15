package dec.haeyum.news.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsItem {

    private String title;
    private String content;
    private String url;


    public NewsItem(String title, String originalNewsPageValue, String substringText, String category) {
        this.title = title;
        this.content = substringText;
        this.url = originalNewsPageValue;
    }

    public NewsItem(String title, String detailPath) {
        this.title = title;
        this.url = detailPath;
    }
}
