package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeTriggerPairTest {

    private LocalTime startTrigger;
    private LocalTime endTrigger;
    private TimeTriggerPair underTest;

    @Before
    public void setUp() {
        startTrigger = new LocalTime(8, 0);
        endTrigger = new LocalTime(12, 0);
        underTest = new TimeTriggerPair(startTrigger, endTrigger);
    }

    @Test
    public void testFireReturnsTrueWhenGivenTimeIsBetweenTheRegisteredRange() throws Exception {
        final Boolean result = underTest.fire(new LocalTime(9, 0).toDateTimeToday().toDate());
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenGivenTimeIsNotBetweenTheRegisteredRange() throws Exception {
        final Boolean result = underTest.fire(new LocalTime(13, 0).toDateTimeToday().toDate());
        assertThat(result, is(false));
    }

    @Test
    public void testFireReturnsTrueWhenGivenTimeIsBetweenTheReversedRange() throws Exception {
        endTrigger = new LocalTime(8, 0);
        startTrigger = new LocalTime(12, 0);
        underTest = new TimeTriggerPair(startTrigger, endTrigger);

        final Boolean result = underTest.fire(new LocalTime(23, 0).toDateTimeToday().toDate());

        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenGivenTimeIsNotBetweenTheReversedRange() throws Exception {
        startTrigger = new LocalTime(12, 0);
        endTrigger = new LocalTime(8, 0);
        underTest = new TimeTriggerPair(startTrigger, endTrigger);

        final Boolean result = underTest.fire(new LocalTime(9, 0).toDateTimeToday().toDate());

        assertThat(result, is(false));
    }

    @Test
    public void testGetTypeReturnsTimeRangeType() throws Exception {
        assertThat(underTest.getType(), is(AchievementType.TIME_RANGE));
    }

    @Test
    public void testGetTimeReturnsRegisteredTriggers() throws Exception {
        assertThat(underTest.getStartTrigger(), is(startTrigger));
        assertThat(underTest.getEndTrigger(), is(endTrigger));
    }
}
