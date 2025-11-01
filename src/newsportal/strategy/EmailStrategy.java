package newsportal.strategy;

import newsportal.article_builder.Article;

public class EmailStrategy implements NotificationStrategy {

    @Override
    public void send(String recipientName, String email, Article article) {
        System.out.printf("Email to %s <%s>: %s%n", recipientName, email, article);
    }

    @Override
    public String name() {
        return "EMAIL";
    }
}
