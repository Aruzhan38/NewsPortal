package newsportal.factory;

import newsportal.strategy.*;

// creates the right notification strategy
public final class NotificationStrategyFactory {

    private NotificationStrategyFactory() {
    }

    public static NotificationStrategy create(String kind) {
        if (kind == null || kind.isBlank()) {
            return new EmailStrategy(); //default
        }

        String type = kind.trim().toUpperCase();

        NotificationStrategy base = switch (type) {
            case "SMS" -> new SMSStrategy();
            case "PUSH" -> new PushStrategy();
            case "EMAIL" -> new EmailStrategy();
            default -> {
                System.out.println("Unknown strategy type: " + kind + " - defaulting to EMAIL.");
                yield new EmailStrategy();
            } };
        // integration of the decorator pattern
        return new newsportal.decorator.LoggingStrategyDecorator(base);
    }

}
