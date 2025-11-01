package newsportal.article_builder;

import java.time.LocalDateTime;
import java.util.List;

public final class ArticleBuilder {
    private String title;
    private String content;
    private Category category;
    private String author;
    private String summary;
    private List<String> tags = List.of();
    private int priority = 0;
    private LocalDateTime publishedAt;

    public ArticleBuilder title(String title) { this.title = title; return this; }
    public ArticleBuilder content(String content) { this.content = content; return this; }
    public ArticleBuilder category(Category category) { this.category = category; return this; }
    public ArticleBuilder author(String author) { this.author = author; return this; }
    public ArticleBuilder summary(String summary) { this.summary = summary; return this; }
    public ArticleBuilder tags(List<String> tags) { this.tags = tags; return this; }
    public ArticleBuilder priority(int priority) { this.priority = priority; return this; }
    public ArticleBuilder publishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; return this; }

    public Article build() {
        if (title == null || title.isBlank())
            throw new IllegalStateException("Article title is required");
        if (content == null || content.isBlank())
            throw new IllegalStateException("Article content is required");
        if (category == null)
            throw new IllegalStateException("Article category is required");

        return new Article(title, content, category, author, summary, tags, priority,
                publishedAt == null ? LocalDateTime.now() : publishedAt
        );
    }
}
