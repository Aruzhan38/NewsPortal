package newsportal;

import java.util.regex.Pattern;

public final class Validation {
    private Validation() {}

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^\\+?\\d[\\d\\s()-]{6,}$");

    public static boolean isNonBlank(String s) { return s != null && !s.isBlank(); }
    public static boolean maxLen(String s, int n) { return s == null || s.length() <= n; }

    public static boolean isEmail(String s) { return isNonBlank(s) && EMAIL.matcher(s).matches(); }
    public static boolean isPhone(String s) { return isNonBlank(s) && PHONE.matcher(s).matches(); }
    public static boolean isToken(String s) { return isNonBlank(s) && s.trim().length() >= 6; }

    public static void require(boolean cond, String message) {
        if (!cond) throw new IllegalArgumentException(message);
    }
}
