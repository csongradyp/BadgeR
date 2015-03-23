package com.noe.badger.util;

import java.util.Date;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateFormatUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    private DateFormatUtil() {
    }

    public static String formatDate(final Date date) {
        return format(date.getTime());
    }

    public static String formatTime(final Date date) {
        return TIME_FORMATTER.print(date.getTime());
    }

    public static Date toDate(final String dateString) {
        return DATE_FORMATTER.parseDateTime(dateString).toDate();
    }

    public static String format(final Long time) {
        return DATE_FORMATTER.print(time);
    }
}
