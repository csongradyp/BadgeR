package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateTriggerTest {

    @Test
    public void testTriggerReturnsTrueWhenGivenDateIsEqualToTheTriggerInDayPrecision() {
        final Date date = new Date();
        final DateTrigger trigger = new DateTrigger(date);

        final Boolean result = trigger.fire(date);

        assertThat(result, is(true));
    }

    @Test
    public void testTriggerReturnsFalseWhenGivenTimeIsNotEqualToTheTrigger() throws Exception {
        final DateTrigger trigger = new DateTrigger(new DateTime().plusDays(1).toDate());

        final Boolean result = trigger.fire(new Date());

        assertThat(result, is(false));
    }

    @Test
    public void testGetTypeReturnsDateType() throws Exception {
        final DateTrigger trigger = new DateTrigger(new Date());
        assertThat(trigger.getType(), is(AchievementType.DATE));
    }

    @Test
    public void testGetTimeReturnsRegisteredTrigger() throws Exception {
        final Date date = new Date();
        final DateTrigger trigger = new DateTrigger(date);
        assertThat(trigger.getDate(), is(date));
    }
}
