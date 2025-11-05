package newsportal.facade;

import newsportal.article_builder.*;
import newsportal.observer.*;
import newsportal.strategy.*;
import newsportal.factory.NotificationStrategyFactory;

import java.util.List;

import static newsportal.Validation.*;

// hides all complexity
public final class NewsPortalFacade {

    private final NewsAgency agency;

    public NewsAgency getAgency(){ return agency; }


    public NewsPortalFacade(NewsAgency agency) {
        this.agency = agency;
    }

    private NotificationStrategy createWrapped(String kind) {
        return NotificationStrategyFactory.create(kind);
    }

    private void validateContactForChannel(String kind, String contact) {
        String k = (kind == null ? "EMAIL" : kind.trim().toUpperCase());
        switch (k) {
            case "EMAIL" -> require(isEmail(contact), "Invalid email");
            case "SMS"   -> require(isPhone(contact), "Invalid phone number");
            case "PUSH"  -> require(isToken(contact), "Invalid push token");
            default      -> throw new IllegalArgumentException("Unknown channel: " + kind);
        }
    }

    public Subscriber quickRegister(String name, String contact, String channelKind) {
        require(notBlank(name), "name required");
        validateContactForChannel(channelKind, contact);

        if (agency.isContactTaken(contact)) {
            throw new IllegalArgumentException("This contact is already registered");
        }
        // Factory паттерн арқылы стратегия құрастыру
        var start = createWrapped(channelKind);
        var s = new Subscriber(name, contact, start);

        // register the subscriber with the central Agency
        if (agency.register(s)) {
            System.out.println("Registered: " + s);
        }
        return s;
    }

    public void changeDelivery(Subscriber subscriber, String newChannelKind, String newContact) {
        validateContactForChannel(newChannelKind, newContact);
        subscriber.setStrategy(createWrapped(newChannelKind));
        subscriber.setContact(newContact);
        System.out.println("Update: " + subscriber.name() + " -> " + subscriber.strategy().name()
                + " (" + subscriber.contact() + ")");
    }

    public void post(Article article) {
        // Мақала шыққаны туралы оқырмандарға хабар беру
        agency.publish(article);
        // Мақаланы файлға сақтау
        ArticleStorage.save(article);
    }
    public void post(String title, String content, Category category,
                     String author, String summary, List<String> tags, int priority) {
        // Builder арқылы construct the complex object
        Article art = new ArticleBuilder()
                .title(title).content(content).category(category)
                .author(author).summary(summary).tags(tags).priority(priority)
                .build();
        post(art);
    }

    public void post(String title, String content, Category category) {
        post(new ArticleBuilder().title(title).content(content).category(category).build());
    }

}
