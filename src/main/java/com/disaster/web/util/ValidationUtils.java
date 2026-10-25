package com.disaster.web.util;

import java.util.regex.Pattern;

/**
 * Utility for input sanitization and validation.
 */
public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+0-9\\s-]{7,20}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.trim()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public static int parsePositiveInt(String val, int defaultValue) {
        if (val == null) return defaultValue;
        try {
            int num = Integer.parseInt(val.trim());
            return Math.max(1, num);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

