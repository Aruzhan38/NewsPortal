package newsportal.observer;

import newsportal.article_builder.Article;
import java.util.ArrayList;
import java.util.List;

public class NewsAgency {
    private final List<Subscriber> subscribers = new ArrayList<>();

    public boolean register(Subscriber sub) {
        if (sub == null) return false;
        subscribers.add(sub);
        return true;
    }

    public boolean unregister(Subscriber sub) {
        if (sub == null) return false;
        boolean ok = subscribers.remove(sub);
        if(ok) {
            System.out.println("Unregistered: " + sub);
        }
        return ok;
    }

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
