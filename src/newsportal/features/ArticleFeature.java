package newsportal.features;

import newsportal.article_builder.*;
import newsportal.facade.NewsPortalFacade;

import java.util.List;
import java.util.Scanner;

public class ArticleFeature {
    private final NewsPortalFacade portal;



    public ArticleFeature(NewsPortalFacade portal) {
        this.portal = portal;
    }

    public void publish(Scanner in) {
        String title   = promptNonBlank(in, "Title: ");
        String content = promptNonBlank(in, "Content: ");
        Category category = askCategory(in);
        System.out.print("Author (optional): ");
        String author  = in.nextLine().trim();
        System.out.print("Summary (optional): ");
        String summary = in.nextLine().trim();
        System.out.print("Tags (comma separated, optional): ");
        String raw = in.nextLine().trim();
        List<String> tags = raw.isBlank() ? List.of() : List.of(raw.split("\\s*,\\s*"));

        Article a = new ArticleBuilder()
                .title(title)
                .content(content)
                .category(category)
                .author(author.isBlank() ? null : author)
                .summary(summary.isBlank() ? null : summary)
                .tags(tags)
                .priority(1)
                .build();

        portal.post(a);
        PublishedCache.ARTICLES.add(a);
    }

    class PublishedCache {
        static final java.util.List<Article> ARTICLES = new java.util.ArrayList<>();
    }

    public void viewPublished(Scanner in) {
        var lines = ArticleStorage.readAll();
        if (lines.isEmpty()) {
            System.out.println("\nNo articles published yet.");
            return;
        }
        System.out.println("\n Published Articles ");
        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ". " + lines.get(i));
        }
        System.out.print("Filter by category (Enter to skip): ");
        String f = in.nextLine().trim().toUpperCase();
        if (!f.isBlank()) {
            lines.stream()
                    .filter(line -> line.startsWith("[" + f + "]"))
                    .forEach(System.out::println);
        } else {
            for (int i=0;i<lines.size();i++) System.out.println((i+1)+". "+lines.get(i));
        }

        System.out.print("\nEnter number for details (Enter to skip): ");
        String s = in.nextLine().trim();
        if (!s.isBlank()) {
            int idx = Integer.parseInt(s) - 1;
            if (idx >= 0 && idx < PublishedCache.ARTICLES.size()) {
                var art = PublishedCache.ARTICLES.get(idx);
                System.out.println("\n" + art.title()
                        + "\nCategory: " + art.category()
                        + "\nAuthor: " + (art.author()==null? "Unknown": art.author())
                        + "\nPublished: " + art.publishedAt()
                        + "\n\n" + art.content());
            }
        }
    }

    private static String promptNonBlank(Scanner in, String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("Value cannot be blank. Try again.");
        }
    }

    private static Category askCategory(Scanner in) {
        while (true) {
            System.out.print("Category (LOCAL/WORLD/TECH/EDUCATION/HEALTH/BUSINESS/SPORTS/CULTURE/ENTERTAINMENT/POLITICS/SCIENCE/MEDIA): ");
            String raw = in.nextLine().trim().toUpperCase();
            try { return Category.valueOf(raw); }
            catch (IllegalArgumentException e) { System.out.println("Unknown category. Try again."); }
        }
    }
}
