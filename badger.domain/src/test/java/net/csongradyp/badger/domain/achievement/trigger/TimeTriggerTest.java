package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.joda.time.LocalTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeTriggerTest {

    @Test
    public void testTriggerReturnsTrueWhenGivenTimeIsEqualToTheTriggerInMinutePrecision() throws Exception {
        final LocalTime time = new LocalTime(12, 12);
        final TimeTrigger trigger = new TimeTrigger(time);

        final Boolean result = trigger.fire(time.toDateTimeToday().toDate());

        assertThat(result, is(true));
    }

    @Test
    public void testTriggerReturnsFalseWhenGivenTimeIsNotEqualToTheTrigger() throws Exception {
        final LocalTime time = new LocalTime(12, 12);
        final TimeTrigger trigger = new TimeTrigger(time);

        final Boolean result = trigger.fire(time.plusMinutes(1).toDateTimeToday().toDate());

        assertThat(result, is(false));
    }

    @Test
    public void testGetTypeReturnsTimeType() throws Exception {
        final TimeTrigger trigger = new TimeTrigger(new LocalTime(12, 12));
        assertThat(trigger.getType(), is(AchievementType.TIME));
    }

    @Test
    public void testGetTimeReturnsRegisteredTrigger() throws Exception {
        final LocalTime time = new LocalTime(12, 12);
        final TimeTrigger trigger = new TimeTrigger(time);
        assertThat(trigger.getTime(), is(time));
    }
}
