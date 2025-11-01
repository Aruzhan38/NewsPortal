package newsportal.strategy;

import newsportal.article_builder.Article;

public class PushStrategy implements NotificationStrategy {

    @Override
    public void send(String recipientName, String deviceToken, Article article) {
        System.out.printf("Push to %s [%s]: %s%n", recipientName, deviceToken, article);
    }

    @Override
    public String name() {
        return "PUSH";
    }
}
