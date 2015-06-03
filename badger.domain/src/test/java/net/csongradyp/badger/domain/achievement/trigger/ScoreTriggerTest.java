package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreTriggerTest {

    @Test
    public void testFireReturnsTrueWhenFiredWithTriggerValue() {
        final ScoreTrigger trigger = new ScoreTrigger(1L);
        final Boolean result = trigger.fire(1L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenTriggeredWithDifferentValueThanTheRegisteredTrigger() {
        final ScoreTrigger trigger = new ScoreTrigger(1L);
        final Boolean result = trigger.fire(2L);
        assertThat(result, is(false));
    }

    @Test
    public void testFireReturnsTrueWhenTriggerOperationIsGreaterThanAndFiredWithGreaterValueThanTheRegisteredTrigger() {
        final ScoreTrigger trigger = new ScoreTrigger(1L, ScoreTrigger.Operation.GREATER_THAN);
        final Boolean result = trigger.fire(10L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsTrueWhenTriggerOperationIsGreaterThanAndFiredWithTriggerValue() {
        final ScoreTrigger trigger = new ScoreTrigger(1L, ScoreTrigger.Operation.GREATER_THAN);
        final Boolean result = trigger.fire(1L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenTriggerOperationIsGreaterThanAndFiredWithLessValueThanTheRegisteredTrigger() {
        final ScoreTrigger trigger = new ScoreTrigger(1L, ScoreTrigger.Operation.GREATER_THAN);
        final Boolean result = trigger.fire(-1L);
        assertThat(result, is(false));
    }

    @Test
    public void testFireReturnsTrueWhenTriggerOperationIsLessThanAndFiredWithLessValueThanTheRegisteredTrigger() {
        final ScoreTrigger trigger = new ScoreTrigger(1L, ScoreTrigger.Operation.LESS_THAN);
        final Boolean result = trigger.fire(0L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsTrueWhenTriggerOperationIsLessThanAndFiredWithTriggerValue() {
        final ScoreTrigger trigger = new ScoreTrigger(1L, ScoreTrigger.Operation.LESS_THAN);
        final Boolean result = trigger.fire(1L);
        assertThat(result, is(true));
    }

    @Test
    public void testFireReturnsFalseWhenTriggerOperationIsLessThanAndFiredWithGreaterValueThanTheRegisteredTrigger() {
        final ScoreTrigger trigger = new ScoreTrigger(1L, ScoreTrigger.Operation.LESS_THAN);
        final Boolean result = trigger.fire(2L);
        assertThat(result, is(false));
    }

    @Test
    public void testGetTypeReturnsScoreType() {
        final ScoreTrigger trigger = new ScoreTrigger(0L);
        assertThat(trigger.getType(), is(AchievementType.SCORE));
    }

    @Test
    public void testGetTriggerReturnsRegisteredTrigger() throws Exception {
        final ScoreTrigger trigger = new ScoreTrigger(100L);
        assertThat(trigger.getTrigger(), is(100L));
    }

    @Test
    public void testGetOperation() {
        final ScoreTrigger triggerEqual = new ScoreTrigger(100L);
        assertThat(triggerEqual.getOperation(), is(ScoreTrigger.Operation.EQUALS));
        final ScoreTrigger triggerGreater = new ScoreTrigger(100L, ScoreTrigger.Operation.GREATER_THAN);
        assertThat(triggerGreater.getOperation(), is(ScoreTrigger.Operation.GREATER_THAN));
        final ScoreTrigger triggerLess = new ScoreTrigger(100L, ScoreTrigger.Operation.LESS_THAN);
        assertThat(triggerLess.getOperation(), is(ScoreTrigger.Operation.LESS_THAN));
    }

    @Test
    public void testParseOperation() {
        final ScoreTrigger.Operation equalTo = ScoreTrigger.Operation.parse("");
        final ScoreTrigger.Operation greaterThan = ScoreTrigger.Operation.parse("+");
        final ScoreTrigger.Operation lessThan = ScoreTrigger.Operation.parse("-");

        assertThat(equalTo, is(ScoreTrigger.Operation.EQUALS));
        assertThat(greaterThan, is(ScoreTrigger.Operation.GREATER_THAN));
        assertThat(lessThan, is(ScoreTrigger.Operation.LESS_THAN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOperationThrowsExceptionWhenInvalidOperatorStringIsGiven() {
        ScoreTrigger.Operation.parse("#");
    }
}
