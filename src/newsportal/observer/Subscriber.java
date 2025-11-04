package newsportal.observer;

import newsportal.article_builder.Article;
import newsportal.strategy.NotificationStrategy;

public class Subscriber {
    private final String name;
    private String contact;
    private NotificationStrategy strategy;

    public Subscriber(String name, String contact, NotificationStrategy strategy) {
        this.name = name;
        this.contact = contact;
        this.strategy = strategy;
    }

    public String name() { return name; }
    public String contact() { return contact; }
    public NotificationStrategy strategy() { return strategy; }

    public void setStrategy(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void notify(Article article) {
        if (strategy != null) {
            strategy.send(name, contact, article);
        } else {
            System.out.println(name + "has no delivery strategy set.");
        }
    }

    @Override
    public String toString() {
        return "Subscriber{" + name + ", via=" + (strategy != null ? strategy.name() : "none") + "}";
    }
}
