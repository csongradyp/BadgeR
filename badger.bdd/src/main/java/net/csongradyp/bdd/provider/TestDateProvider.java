package net.csongradyp.bdd.provider;

import java.util.Date;
import javax.inject.Named;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Named
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
        stubDateTime(date, time);
    }

    public void stubDateTime(final String date, final String time) {
        stubbedDate = dateTimeFormatter.parseDateTime(date + " " + time).toDate();
    }

    @Override
    public String currentDateString() {
        return format(stubbedDate.getTime());
    }

    @Override
    public String currentTimeString() {
        return getTime(stubbedDate);
    }

    @Override
    public Date currentDate() {
        return stubbedDate;
    }

    @Override
    public Date currentTime() {
        return stubbedDate;
    }
}
