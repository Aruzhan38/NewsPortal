package newsportal;

import java.util.regex.Pattern;

// helper class for input validation
public final class Validation {

    private Validation() {}

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?\\d[\\d\\s()-]{6,}$");

    public static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    public static boolean isEmail(String s) {
        return notBlank(s) && EMAIL_PATTERN.matcher(s).matches();
    }

    public static boolean isPhone(String s) {
        return notBlank(s) && PHONE_PATTERN.matcher(s).matches();
    }

    public static boolean isToken(String s) {
        return notBlank(s) && s.trim().length() >= 6;
    }

    // быстрая проверка входных данных перед выполнением бизнес-логики
    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
