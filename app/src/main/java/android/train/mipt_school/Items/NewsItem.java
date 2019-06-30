package android.train.mipt_school.Items;

public class NewsItem {
    private String title;
    private String description;
    private String date;
    private String link;

    public NewsItem(String title, String description, String newsDate, String link) {
        this.title = title;
        this.description = description;
        this.date = newsDate.substring(0, 1).toUpperCase() + newsDate.substring(1);
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }
}
