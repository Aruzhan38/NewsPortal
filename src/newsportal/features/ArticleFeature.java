package newsportal.features;

import newsportal.article_builder.*;
import newsportal.facade.NewsPortalFacade;

import java.util.List;
import java.util.Scanner;

// handles everything related to publishing and viewing articles
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
    }

    public void viewPublished(Scanner in) {
        // reading saved article lines from storage
        var lines = ArticleStorage.readAll();
        if (lines.isEmpty()) {
            System.out.println("\nNo articles published yet.");
            return;
        }
        System.out.println("\n Published Articles ");
        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ". " + lines.get(i));
        }
    }

    // повторяет запрос до тех пор, пока пользователь не введет непустое значение
    private static String promptNonBlank(Scanner in, String label) {
        while (true) {
            System.out.print(label);
            String s = in.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("Value cannot be blank. Try again.");
        }
    }

    // повторяет запрос, пока пользователь не введет действительное значение из Category
    private static Category askCategory(Scanner in) {
        while (true) {
            System.out.print("Category (LOCAL/WORLD/TECH/EDUCATION/HEALTH/BUSINESS/SPORTS/CULTURE/ENTERTAINMENT/POLITICS/SCIENCE/MEDIA): ");
            String raw = in.nextLine().trim().toUpperCase();
            try { return Category.valueOf(raw); }
            catch (IllegalArgumentException e) { System.out.println("Unknown category. Try again."); }
        }
    }
}
