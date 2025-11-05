package newsportal.observer;

import newsportal.article_builder.Article;
import java.util.ArrayList;
import java.util.List;

// acts as the publisher that notifies all subscribers when a new article is published
public class NewsAgency {
    // список подписчиков
    private final List<Subscriber> subscribers = new ArrayList<>();

    // attach
    public boolean register(Subscriber sub) {
        if (sub == null) return false;
        subscribers.add(sub);
        return true;
    }

    // unique
    public boolean isContactTaken(String contact) {
        for (Subscriber s : subscribers) {
            if (s.contact().equalsIgnoreCase(contact)) return true;
        }
        return false;
    }

    // detach
    public boolean unregister(Subscriber sub) {
        if (sub == null) return false;
        boolean ok = subscribers.remove(sub);
        if(ok) {
            System.out.println("Unregistered: " + sub);
        }
        return ok;
    }

    // публикация и уведомление
    public void publish(Article article) {
        System.out.println("\n Publishing: " + article);
        notifyAllSubscribers(article);
    }

    public List<Subscriber> list() {
        return List.copyOf(subscribers);
    }

    private void notifyAllSubscribers(Article article) {
        for(Subscriber sub : subscribers) {
            sub.notify(article);
        }
    }
}
