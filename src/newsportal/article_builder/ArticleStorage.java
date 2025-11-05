package newsportal.article_builder;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// saves published articles into a .txt file and reads them back
public class ArticleStorage {

    private static final String FILE_PATH = "article_builder/articles.txt";

    static {
        try {
            // осы package бар екенін растайды
            Files.createDirectories(Paths.get("article_builder"));
            // егер file болмаса, жаңасын ашады
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
            }
        } catch (IOException e) {
            System.out.println(" Could not initialize storage: " + e.getMessage());
        }
    }

    // мақаланы сақтайды
    public static void save(Article article) {
        //ақпарат файлдың соңына қосылады
        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            fw.write(format(article) + System.lineSeparator());
        } catch (IOException e) {
            System.out.println(" Error saving article: " + e.getMessage());
        }
    }

    // мақала туралы ақпаратты шығару үшін қажет
    public static List<String> readAll() {
        try {
            // толық файлды оқиды
            return Files.readAllLines(Paths.get(FILE_PATH));
        } catch (IOException e) {
            System.out.println(" Error reading file: " + e.getMessage());
            return List.of();
        }
    }

    private static String format(Article a) {

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

        return String.format("[%s] %s — %s (%s)",
                a.category(),
                a.title(),
                a.author() != null ? a.author() : "Unknown",
                a.publishedAt().format(fmt));
    }
}