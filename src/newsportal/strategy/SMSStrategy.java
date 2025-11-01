package newsportal.strategy;

import newsportal.article_builder.Article;

public class SMSStrategy implements NotificationStrategy {

    @Override
    public void send(String recipientName, String phone, Article article) {
        System.out.printf("SMS to %s (%s): %s%n", recipientName, phone, article);
    }

    @Override
    public String name() {
        return "SMS";
    }
}
