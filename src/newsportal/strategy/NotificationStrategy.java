package newsportal.strategy;

import newsportal.article_builder.Article;

// common interface for all strategies
public interface NotificationStrategy {
    // отправка уведомления
    void send(String recipientName, String contact, Article article);
   // имя стратегии
    String name();
}
