package newsportal.strategy;

import newsportal.article_builder.Article;

public interface NotificationStrategy {
    void send(String recipientName, String contact, Article article);
    String name();
}
