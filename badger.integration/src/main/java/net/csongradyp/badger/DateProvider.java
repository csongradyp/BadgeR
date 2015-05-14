package net.csongradyp.badger;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Named;
import java.util.Date;

@Named
public class DateProvider implements IDateProvider {

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    public DateProvider() {
        dateFormatter = DateTimeFormat.forPattern("MM-dd");
        timeFormatter = DateTimeFormat.forPattern("HH:mm");
    }

    @Override
    public String currentDate() {
        return format(new Date().getTime());
    }

    @Override
    public String currentTime() {
        return getTime(new Date());
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
        final LocalTime currentTime = new DateTime().toLocalTime();
        final LocalTime triggerTime = new DateTime(date).toLocalTime();
        return currentTime.isBefore(triggerTime) || currentTime.isEqual(triggerTime);
    }

    @Override
    public Boolean isCurrentTimeAfter(final Date date) {
        final LocalTime currentTime = new DateTime().toLocalTime();
        final LocalTime triggerTime = new DateTime(date).toLocalTime();
        return currentTime.isAfter(triggerTime) || currentTime.isEqual(triggerTime);
    }

    @Override
    public String getTime(final Date date) {
        return timeFormatter.print(date.getTime());
    }

}
