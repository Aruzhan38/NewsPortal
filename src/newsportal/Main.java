package newsportal;

import newsportal.features.ArticleFeature;
import newsportal.features.SubscriberFeature;
import newsportal.facade.NewsPortalFacade;
import newsportal.observer.NewsAgency;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        var agency = new NewsAgency();
        var portal = new NewsPortalFacade(agency);

        var subs = new SubscriberFeature(agency, portal);
        var arts = new ArticleFeature(portal);

        System.out.println("=== 📰 INTERACTIVE NEWS PORTAL SYSTEM ===\n");

        int num = askPositiveInt(in, "Enter number of subscribers: ");
        subs.initialRegistration(in, num);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Publish new article");
            System.out.println("2. Change subscriber delivery strategy");
            System.out.println("3. Unsubscribe a user");
            System.out.println("4. View published articles");
            System.out.println("5. Add subscriber");
            System.out.println("6. List subscribers");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> arts.publish(in);
                case "2" -> subs.changeDelivery(in);
                case "3" -> subs.unsubscribe(in);
                case "4" -> arts.viewPublished(in);
                case "5" -> subs.initialRegistration(in, 1);
                case "6" -> subs.listSubscribers();
                case "0" -> { System.out.println("👋 Exiting program..."); in.close(); return; }
                default -> System.out.println("⚠️ Invalid option, please try again!");
            }
        }
    }

    private static int askPositiveInt(Scanner in, String label) {
        int n = 0;
        while (n <= 0) {
            System.out.print(label);
            try {
                n = Integer.parseInt(in.nextLine().trim());
                if (n <= 0) System.out.println("⚠️ Must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Not a number. Try again.");
            }
        }
        return n;
    }
}
