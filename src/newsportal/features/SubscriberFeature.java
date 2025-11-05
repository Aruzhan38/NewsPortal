package newsportal.features;

import newsportal.Validation;
import newsportal.facade.NewsPortalFacade;
import newsportal.observer.NewsAgency;
import newsportal.observer.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// handles registration, changing strategy, and unsubscribing
public class SubscriberFeature {
    private final NewsAgency agency;
    private final NewsPortalFacade portal;

    // список объектов Subscriber
    private final List<Subscriber> subscribers = new ArrayList<>();

    public SubscriberFeature(NewsAgency agency, NewsPortalFacade portal) {
        this.agency = agency;
        this.portal = portal;
    }

    public List<Subscriber> getSubscribers() { return subscribers; }

    public void initialRegistration(Scanner in, int num) {
        for (int i = 1; i <= num; i++) {
            System.out.printf("\n[%d] Subscriber name: ", i);
            String name = promptNonBlank(in, "");
            String channel = readChannel(in);
            String contact = readContactForChannel(in, channel);
            subscribers.add(portal.quickRegister(name, contact, channel));
        }
    }

    // смена стратегии
    public void changeDelivery(Scanner in) {
        String name = promptNonBlank(in, "Enter subscriber name: ");
        Subscriber s = findByName(name);
        if (s == null) { System.out.println("Subscriber not found!"); return; }

        String newKind = readChannel(in);
        String newContact = readContactForChannel(in, newKind);
        portal.changeDelivery(s, newKind, newContact);

        System.out.println("Current: " + s.strategy().name() + " (" + s.contact() + ")");

    }

    public void unsubscribe(Scanner in) {
        String name = promptNonBlank(in, "Enter subscriber name to remove: ");
        Subscriber s = findByName(name);
        if (s == null) { System.out.println("Subscriber not found!"); return; }
        agency.unregister(s);
        subscribers.remove(s);
    }

    public void listSubscribers() {
        if (subscribers.isEmpty()) { System.out.println("No subscribers yet."); return; }
        System.out.println("\n=== Subscribers ===");
        for (Subscriber s : subscribers) System.out.println("• " + s);
    }

    // быстрый поиск подписчиков по имени
    private Subscriber findByName(String name) {
        for (Subscriber s : subscribers)
            if (s.name().equalsIgnoreCase(name)) return s;
        return null;
    }

    private static String promptNonBlank(Scanner in, String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("Value cannot be blank. Try again.");
        }
    }

    // синонимы
    private static String readChannel(Scanner in) {
        while (true) {
            System.out.print("Delivery channel (EMAIL / SMS / PUSH): ");
            String raw = in.nextLine().trim().toUpperCase();
            switch (raw) {
                case "EMAIL", "E-MAIL", "MAIL": return "EMAIL";
                case "SMS", "PHONE", "TEL":     return "SMS";
                case "PUSH", "TOKEN", "APP":    return "PUSH";
                default: {  System.out.println("Unknown channel. Please enter EMAIL, SMS, or PUSH.");}
            }
        }
    }

    // валидация
    private static String readContactForChannel(Scanner in, String channel) {
        while (true) {
            System.out.print("Contact (" + channel + "): ");
            String contact = in.nextLine().trim();
            boolean ok = switch (channel) {
                case "EMAIL" -> Validation.isEmail(contact);
                case "SMS"   -> Validation.isPhone(contact);
                case "PUSH"  -> Validation.isToken(contact);
                default      -> false;
            };
            if (ok) return contact;

            System.out.println("Invalid " + channel.toLowerCase()
                    + (channel.equals("EMAIL") ? " (e.g., user@example.com)"
                    : channel.equals("SMS")   ? " (e.g., +77011234567)"
                    : " (e.g., token-xyz12345)"));
        }
    }
}

