package net.csongradyp.provider;

import net.csongradyp.badger.DateProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class TestDateProvider extends DateProvider {

    private Date stubbedDate;

    private final DateTimeFormatter dateTimeFormatter;
    private final DateTimeFormatter dateFormatter;

    public TestDateProvider() {
        dateFormatter = DateTimeFormat.forPattern("MM-dd");
        dateTimeFormatter = DateTimeFormat.forPattern("MM-dd HH:mm");
    }

    public void stubDate(final String date) {
        this.stubbedDate = dateFormatter.parseDateTime(date).toDate();
    }

    public void stubTime(final String time) {
        final String date = dateFormatter.print(new DateTime().minusDays(1));
        stubbedDate = dateTimeFormatter.parseDateTime(date + " " + time).toDate();
    }

    public void stubDateTime(final String date, final String time) {
        stubbedDate = dateTimeFormatter.parseDateTime(date + " " + time).toDate();
    }

    @Override
    public String now() {
        return format(stubbedDate.getTime());
    }

    @Override
    public String currentTime() {
        return getTime(stubbedDate);
    }

    @Override
    public Boolean isCurrentTimeBefore(final Date date) {
        final LocalTime currentTime = new DateTime(stubbedDate).toLocalTime();
        final LocalTime triggerTime = new DateTime(date).toLocalTime();
        return currentTime.isBefore(triggerTime) || currentTime.isEqual(triggerTime);
    }

    @Override
    public Boolean isCurrentTimeAfter(final Date date) {
        final LocalTime currentTime = new DateTime(stubbedDate).toLocalTime();
        final LocalTime triggerTime = new DateTime(date).toLocalTime();
        return currentTime.isAfter(triggerTime) || currentTime.isEqual(triggerTime);
    }
}
