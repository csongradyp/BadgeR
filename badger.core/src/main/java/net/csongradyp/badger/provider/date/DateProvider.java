package net.csongradyp.badger.provider.date;

import java.util.Date;
import javax.inject.Named;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Named
public class DateProvider implements IDateProvider {

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    public DateProvider() {
        dateFormatter = DateTimeFormat.forPattern("MM-dd");
        timeFormatter = DateTimeFormat.forPattern("HH:mm");
    }

    @Override
    public String currentDateString() {
        return format(new Date().getTime());
    }

    @Override
    public Date currentDate() {
        return new Date();
    }

    @Override
    public String currentTimeString() {
        return getTime(new Date());
    }

    @Override
    public Date currentTime() {
        return new Date();
    }

    @Override
    public String getDate(final Date date) {
        return format(date.getTime());
    }

    @Override
    public Date parseDate(final String date) {
        return dateFormatter.parseLocalDate(date).toDate();
    }

    protected String format(final Long time) {
        return dateFormatter.print(time);
    }

    @Override
    public String getTime(final Date date) {
        return timeFormatter.print(date.getTime());
    }

    @Override
    public LocalTime parseTime(final String time) {
        return timeFormatter.parseLocalTime(time);
    }

}
