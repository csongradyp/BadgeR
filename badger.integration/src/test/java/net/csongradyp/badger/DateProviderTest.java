package net.csongradyp.badger;

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
    public void testCurrentDateReturnsCurrentDateInDayPrecisionAsAString() {
        final String currentDate = underTest.currentDate();
        assertThat(currentDate, is(equalTo(dateFormatter.print(new DateTime()))));
    }

    @Test
    public void testGetDateReturnsGivenDateInDayPrecisionAsAString() {
        final DateTime date = new DateTime(2015, 10, 15, 12, 0);
        final String result = underTest.getDate(date.toDate());
        assertThat(result, is(equalTo("10-15")));
    }

    @Test
    public void testCurrentTimeReturnsCurrentTimeOnlyInMinutePrecisionAsAString() {
        final String currentTime = underTest.currentTime();
        assertThat(currentTime, is(equalTo(timeFormatter.print(new DateTime()))));
    }

    @Test
    public void testGetTimeReturnsGivenDatesTimeOnlyInMinutePrecisionAsAString() {
        final DateTime date = new DateTime(2015, 10, 10, 12, 23, 55);
        final String currentTime = underTest.getTime(date.toDate());
        assertThat(currentTime, is(equalTo("12:23")));
    }

    @Test
    public void testIsCurrentTimeBeforeReturnsTrueWhenCurrentDateIsBeforeTheGivenDate() throws Exception {
        DateTime date = new DateTime().plusMinutes(1);
        final Boolean isBeforeOneMinute = underTest.isCurrentTimeBefore(date.toDate());
        assertThat(isBeforeOneMinute, is(true));

        date = new DateTime().plusSeconds(1);
        final Boolean isBeforeOneSecond = underTest.isCurrentTimeBefore(date.toDate());
        assertThat(isBeforeOneSecond, is(true));
    }

    @Test
    public void testIsCurrentTimeBeforeReturnsTrueWhenCurrentDateIsEqualToTheGivenDate() throws Exception {
        DateTime date = new DateTime();
        final Boolean isEqualTime = underTest.isCurrentTimeBefore(date.toDate());
        assertThat(isEqualTime, is(true));
    }

    @Test
    public void testIsCurrentTimeAfterReturnsTrueWhenCurrentDateIsAfterTheGivenDate() throws Exception {
        DateTime date = new DateTime().minusMinutes(1);
        final Boolean isAfterOneMinute = underTest.isCurrentTimeAfter(date.toDate());
        assertThat(isAfterOneMinute, is(true));

        date = new DateTime().minusSeconds(1);
        final Boolean isAfterOneSecond = underTest.isCurrentTimeAfter(date.toDate());
        assertThat(isAfterOneSecond, is(true));
    }

    @Test
    public void testIsCurrentTimeAfterReturnsTrueWhenCurrentDateIsEqualToTheGivenDate() throws Exception {
        DateTime date = new DateTime();
        final Boolean isEqualTime = underTest.isCurrentTimeAfter(date.toDate());
        assertThat(isEqualTime, is(true));
    }
}
