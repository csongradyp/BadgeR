package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreTriggerPairTest {

    private Long startTrigger;
    private Long endTrigger;
    private ScoreTriggerPair underTest;

    @Before
    public void setUp() {
        startTrigger = 0L;
        endTrigger = 10L;
        underTest = new ScoreTriggerPair(startTrigger, endTrigger);
    }

    @Test
    public void testFireReturnsTrueWhenGivenScoreIsBetweenTheRegisteredRange() {
        final Boolean result = underTest.fire(5L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenGivenScoreIsNotBetweenTheRegisteredRange() {
        final Boolean result = underTest.fire(11L);
        assertThat(result, is(false));
    }

    @Test
    public void testFireReturnsTrueWhenGivenScoreIsBetweenTheReversedRange() {
        underTest = new ScoreTriggerPair(endTrigger, startTrigger);
        final Boolean result = underTest.fire(11L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenGivensScoreIsNotBetweenTheReversedRange() {
        underTest = new ScoreTriggerPair(endTrigger, startTrigger);
        final Boolean result = underTest.fire(5L);
        assertThat(result, is(false));
    }

    @Test
    public void testGetTypeReturnsTimeRangeType() {
        assertThat(underTest.getType(), is(AchievementType.SCORE_RANGE));
    }

    @Test
    public void testGetTriggerReturnsRegisteredTriggers() {
        assertThat(underTest.getStartTrigger(), is(startTrigger));
        assertThat(underTest.getEndTrigger(), is(endTrigger));
    }
}
