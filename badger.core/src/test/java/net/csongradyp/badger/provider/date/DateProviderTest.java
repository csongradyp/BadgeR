package net.csongradyp.badger.provider.date;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateProviderTest {

    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;

    private DateProvider underTest;

    @Before
    public void setUp() {
        dateFormatter = DateTimeFormat.forPattern("MM-dd");
        timeFormatter = DateTimeFormat.forPattern("HH:mm");
        underTest = new DateProvider();
    }

    @Test
    public void testCurrentDateStringReturnsCurrentDateInDayPrecisionAsAString() {
        final String currentDate = underTest.currentDateString();
        assertThat(currentDate, is(equalTo(dateFormatter.print(new DateTime()))));
    }

    @Test
    public void testCurrentDateReturnsCurrentDateInDayPrecision() {
        final Date currentDate = underTest.currentDate();
        assertThat(currentDate, is(equalTo(new Date())));
    }

    @Test
    public void testGetDateReturnsGivenDateInDayPrecisionAsAString() {
        final DateTime date = new DateTime(2015, 10, 15, 12, 0);
        final String result = underTest.getDate(date.toDate());
        assertThat(result, is(equalTo("10-15")));
    }

    @Test
    public void testCurrentTimeStringReturnsCurrentTimeOnlyInMinutePrecisionAsAString() {
        final String currentTime = underTest.currentTimeString();
        assertThat(currentTime, is(equalTo(timeFormatter.print(new DateTime()))));
    }

    @Test
    public void testCurrentTimeReturnsCurrentTimeOnlyInMinutePrecision() {
        final Date currentTime = underTest.currentTime();
        assertThat(currentTime, is(equalTo(new Date())));
    }

    @Test
    public void testGetTimeReturnsGivenDatesTimeOnlyInMinutePrecisionAsAString() {
        final DateTime date = new DateTime(2015, 10, 10, 12, 23, 55);
        final String currentTime = underTest.getTime(date.toDate());
        assertThat(currentTime, is(equalTo("12:23")));
    }
}
