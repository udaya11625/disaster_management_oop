package com.disaster.web.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility for formatting timestamps and calculating relative time.
 */
public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm");
    private static final SimpleDateFormat SHORT_DATE = new SimpleDateFormat("MMM dd, yyyy");

    public static String formatTimestamp(Timestamp ts) {
        if (ts == null) return "N/A";
        synchronized (DATE_FORMAT) {
            return DATE_FORMAT.format(new Date(ts.getTime()));
        }
    }

    public static String formatShortDate(Timestamp ts) {
        if (ts == null) return "N/A";
        synchronized (SHORT_DATE) {
            return SHORT_DATE.format(new Date(ts.getTime()));
        }
    }

    public static String timeAgo(Timestamp ts) {
        if (ts == null) return "N/A";
        Instant past = ts.toInstant();
        Instant now = Instant.now();

        long minutes = ChronoUnit.MINUTES.between(past, now);
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + "m ago";

        long hours = ChronoUnit.HOURS.between(past, now);
        if (hours < 24) return hours + "h ago";

        long days = ChronoUnit.DAYS.between(past, now);
        if (days < 30) return days + "d ago";

        return formatShortDate(ts);
    }
}

