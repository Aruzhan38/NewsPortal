package newsportal;

import newsportal.article_builder.*;
import newsportal.facade.NewsPortalFacade;
import newsportal.observer.NewsAgency;
import newsportal.observer.Subscriber;

import java.util.*;

import static newsportal.Validation.*;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        NewsAgency agency = new NewsAgency();
        NewsPortalFacade portal = new NewsPortalFacade(agency);

        System.out.println("=== 📰 INTERACTIVE NEWS PORTAL SYSTEM ===\n");

        int num = 0;
        while (num <= 0) {
            System.out.print("Enter number of subscribers: ");
            try {
                num = Integer.parseInt(in.nextLine().trim());
                if (num <= 0) System.out.println("⚠️ Must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Not a number. Try again.");
            }
        }

        List<Subscriber> subscribers = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            System.out.printf("\n[%d] Subscriber name: ", i);
            String name = promptNonBlank(in, "");
            String channel = readChannel(in);
            String contact = readContactForChannel(in, channel);

            try {
                subscribers.add(portal.quickRegister(name, contact, channel));
            } catch (IllegalArgumentException ex) {
                System.out.println("❌ " + ex.getMessage());
                i--;
            }
        }

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Publish new article");
            System.out.println("2. Change subscriber delivery strategy");
            System.out.println("3. Unsubscribe a user");
            System.out.println("4. View published articles");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Title: ");
                    String title = in.nextLine();
                    System.out.print("Content: ");
                    String content = in.nextLine();
                    Category category = askCategory(in);
                    System.out.print("Author: ");
                    String author = in.nextLine();
                    System.out.print("Summary(optional): ");
                    String summary = in.nextLine();
                    System.out.print("Tags (comma separated - also optional): ");
                    String raw = in.nextLine().trim();
                    List<String> tags = raw.isBlank() ? List.of()
                            : List.of(raw.split("\\s*,\\s*"));

                    try {
                        Article article = new ArticleBuilder()
                                .title(title)
                                .content(content)
                                .category(category)
                                .author(author.isBlank() ? null : author)
                                .summary(summary.isBlank() ? null : summary)
                                .tags(tags)
                                .priority(1)
                                .build();

                        portal.post(article);
                    } catch (IllegalArgumentException ex) {
                        System.out.println("❌ " + ex.getMessage());
                    }
                }

                case "2" -> {
                    String name = promptNonBlank(in, "Enter subscriber name: ");
                    Subscriber s = findByName(subscribers, name);
                    if (s == null) {
                        System.out.println("❌ Subscriber not found!");
                        break;
                    }

                    String newKind = readChannel(in);
                    String newContact = readContactForChannel(in, newKind);

                    try {
                        portal.changeDelivery(s, newKind, newContact);
                    } catch (IllegalArgumentException ex) {
                        System.out.println("❌ " + ex.getMessage());
                    }
                }

                case "3" -> {
                    String name = promptNonBlank(in, "Enter subscriber name to remove: ");
                    Subscriber s = findByName(subscribers, name);
                    if (s != null) {
                        agency.unregister(s);
                        subscribers.remove(s);
                    } else {
                        System.out.println("❌ Subscriber not found!");
                    }
                }

                case "4" -> {
                    System.out.println("\n=== 🗞️ Published Articles ===");
                    var articles = newsportal.article_builder.ArticleStorage.readAll();
                    if (articles.isEmpty()) {
                        System.out.println("No articles published yet.");
                    } else {
                        for (String line : articles) {
                            System.out.println("• " + line);
                        }
                    }
                }


                case "0" -> {
                    System.out.println("👋 Exiting program...");
                    in.close();
                    return;
                }

                default -> System.out.println("⚠️ Invalid option, please try again!");
            }
        }
    }

    private static Category askCategory(Scanner in) {
        while (true) {
            System.out.print("Category (LOCAL/WORLD/TECH/EDUCATION/HEALTH/BUSINESS/SPORTS/CULTURE/ENTERTAINMENT/POLITICS/SCIENCE/MEDIA): ");
            String raw = in.nextLine().trim().toUpperCase();
            try {
                return Category.valueOf(raw);
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Unknown category. Try again.");
            }
        }
    }

    private static Subscriber findByName(List<Subscriber> subs, String name) {
        for (Subscriber s : subs) {
            if (s.name().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    private static String promptNonBlank(Scanner in, String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("❌ Value cannot be blank. Try again.");
        }
    }

    private static String readChannel(Scanner in) {
        while (true) {
            System.out.print("Delivery channel (EMAIL / SMS / PUSH): ");
            String raw = in.nextLine().trim().toUpperCase();
            switch (raw) {
                case "EMAIL", "E-MAIL", "MAIL" -> {
                    return "EMAIL";
                }
                case "SMS", "PHONE", "TEL" -> {
                    return "SMS";
                }
                case "PUSH", "TOKEN", "APP" -> {
                    return "PUSH";
                }
                default -> System.out.println("❌ Unknown channel. Please enter EMAIL, SMS, or PUSH.");
            }
        }
    }


    private static String readContactForChannel(Scanner in, String channel) {
        while (true) {
            System.out.print("Contact (" + channel + "): ");
            String contact = in.nextLine().trim();
            boolean ok = switch (channel) {
                case "EMAIL" -> isEmail(contact);
                case "SMS" -> isPhone(contact);
                case "PUSH" -> isToken(contact);
                default -> false;
            };
            if (ok) return contact;

            System.out.println("❌ Invalid " + channel.toLowerCase() + (channel.equals("EMAIL")
                    ? " (e.g., user@example.com)"
                    : channel.equals("SMS")
                    ? " (e.g., +77011234567)"
                    : " (e.g., token-xyz12345)"));
        }
    }
}


