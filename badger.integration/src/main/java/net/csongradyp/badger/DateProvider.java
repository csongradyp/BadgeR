package net.csongradyp.badger;

import java.util.Date;
import javax.inject.Named;
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
    public String now() {
        return format(new Date().getTime());
    }

    @Override
    public String getDate(final Date date) {
        return format(date.getTime());
    }

    protected String format(final Long time) {
        return dateFormatter.print(time);
    }

    @Override
    public Boolean isCurrentTimeBefore(final Date date) {
        return new Date().before(date);
    }

    @Override
    public Boolean isCurrentTimeAfter(final Date date) {
        return new Date().after(date);
    }

    @Override
    public String getTime(final Date date) {
        return timeFormatter.print(date.getTime());
    }

    public Date asDate(final String dateString) {
        return dateFormatter.parseDateTime(dateString).toDate();
    }
}
