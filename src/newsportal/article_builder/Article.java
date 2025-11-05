package newsportal.article_builder;

import java.time.LocalDateTime;
import java.util.List;

// model class that holds all article information
public class Article {
    private final String title;
    private final String content;
    private final Category category;
    private final String author;
    private final String summary;
    private final List<String> tags;
    private final int priority;
    private final LocalDateTime publishedAt;

    public Article(String title, String content, Category category, String author,
                   String summary, List<String> tags, int priority, LocalDateTime publishedAt) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.summary = summary;
        this.tags = tags;
        this.priority = priority;

        if (publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        } else {
            this.publishedAt = publishedAt;
        }
    }
    public String title() { return title; }
    public String content() { return content; }
    public Category category() { return category; }
    public String author() { return author; }
    public String summary() { return summary; }
    public List<String> tags() { return tags; }
    public int priority() { return priority; }
    public LocalDateTime publishedAt() { return publishedAt; }

    @Override
    public String toString() {

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

        return "[" + category + (priority > 0 ? " P" + priority : "") + "] "
                + title + (author != null ? " — " + author : "")
                + " (" + publishedAt.format(fmt) + ")";
    }
}
