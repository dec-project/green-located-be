package dec.haeyum.news.dto.response;


import lombok.Getter;

@Getter
public class NewsItem {

    private String category;
    private String title;
    private String content;
    private String url;


    public NewsItem(String title, String originalNewsPageValue, String substringText, String category) {
        this.category = category;
        this.title = title;
        this.content = substringText;
        this.url = originalNewsPageValue;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
