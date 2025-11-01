package newsportal.facade;

import newsportal.article_builder.*;
import newsportal.observer.*;
import newsportal.strategy.*;

import java.util.List;

import static newsportal.Validation.*;

public final class NewsPortalFacade {

    private final NewsAgency agency;
    public NewsAgency getAgency(){ return agency; }


    public NewsPortalFacade(NewsAgency agency) {
        this.agency = agency;
    }

    private NotificationStrategy createWrapped(String kind) {
        return switch (kind.toUpperCase()) {
            case "EMAIL" -> new EmailStrategy();
            case "SMS"   -> new SMSStrategy();
            case "PUSH"  -> new PushStrategy();
            default -> throw new IllegalArgumentException("Unknown channel: " + kind);
        };
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
        require(isNonBlank(name), "name required");
        validateContactForChannel(channelKind, contact);
        var start = createWrapped(channelKind);
        var s = new Subscriber(name, contact, start);

        if (agency.register(s)) {
            System.out.println("Registered: " + s);
        }
        return s;
    }

    public void changeDelivery(Subscriber subscriber, String newChannelKind, String newContact) {
        validateContactForChannel(newChannelKind, newContact);
        subscriber.setStrategy(createWrapped(newChannelKind));
        subscriber.setContact(newContact);
        System.out.println("🔄 " + subscriber.name() + " -> " + subscriber.strategy().name()
                + " (" + subscriber.contact() + ")");
    }

    public void post(Article article) {
        agency.publish(article);
        ArticleStorage.save(article);
    }
    public void post(String title, String content, Category category,
                     String author, String summary, List<String> tags, int priority) {
        Article art = new ArticleBuilder()
                .title(title)
                .content(content)
                .category(category)
                .author(author)
                .summary(summary)
                .tags(tags)
                .priority(2)
                .build();
        agency.publish(art);
    }

    public void post(String title, String content, Category category) {
        Article a = new ArticleBuilder()
                .title(title)
                .content(content)
                .category(category)
                .build();
        agency.publish(a);
    }

}
