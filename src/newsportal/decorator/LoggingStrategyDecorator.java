package newsportal.decorator;

import newsportal.article_builder.Article;
import newsportal.strategy.NotificationStrategy;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// wraps around a notification strategy
public class LoggingStrategyDecorator implements NotificationStrategy {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final NotificationStrategy wrapped;

    public LoggingStrategyDecorator(NotificationStrategy wrapped) {
        this.wrapped = wrapped;
    }

    // қандай мақала қандай стратегия арқылы оқырманға жетеді
    @Override
    public void send(String recipientName, String contact, Article article) {
        String now = LocalTime.now().format(fmt);
        System.out.printf("[LOG %s] %s via %s -> \"%s\"%n",
                now, recipientName, wrapped.name(), article.title());
        wrapped.send(recipientName, contact, article);
    }

    @Override
    public String name() {
        return wrapped.name();
    }
}
