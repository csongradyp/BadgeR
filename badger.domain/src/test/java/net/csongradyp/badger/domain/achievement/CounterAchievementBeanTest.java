package net.csongradyp.badger.domain.achievement;

import java.util.List;
import net.csongradyp.badger.domain.achievement.trigger.NumberTrigger;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CounterAchievementBeanTest {

    private CounterAchievementBean underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new CounterAchievementBean();
    }

    @Test
    public void testCounterAchievementBeanWithEqualTrigger() {
        final String[] triggers = {"10", "20"};

        underTest.setTrigger(triggers);
        final List<NumberTrigger> parsedTriggers = underTest.getTrigger();

        assertThat(parsedTriggers.size(), is(equalTo(2)));
        assertThat(parsedTriggers.get(0).getOperation(), is(NumberTrigger.Operation.EQUALS));
        assertThat(parsedTriggers.get(0).getTrigger(), is(equalTo(10L)));
        assertThat(parsedTriggers.get(1).getOperation(), is(NumberTrigger.Operation.EQUALS));
        assertThat(parsedTriggers.get(1).getTrigger(), is(equalTo(20L)));
    }

    @Test
    public void testCounterAchievementBeanWithGreaterThanTrigger() {
        final String[] triggers = {"11+", "22+"};

        underTest.setTrigger(triggers);
        final List<NumberTrigger> parsedTriggers = underTest.getTrigger();

        assertThat(parsedTriggers.size(), is(equalTo(2)));
        assertThat(parsedTriggers.get(0).getOperation(), is(NumberTrigger.Operation.GREATER_THAN));
        assertThat(parsedTriggers.get(0).getTrigger(), is(equalTo(11L)));
        assertThat(parsedTriggers.get(1).getOperation(), is(NumberTrigger.Operation.GREATER_THAN));
        assertThat(parsedTriggers.get(1).getTrigger(), is(equalTo(22L)));
    }

    @Test
    public void testCounterAchievementBeanWithLessThanTrigger() {
        final String[] triggers = {"12-", "23-"};

        underTest.setTrigger(triggers);
        final List<NumberTrigger> parsedTriggers = underTest.getTrigger();

        assertThat(parsedTriggers.size(), is(equalTo(2)));
        assertThat(parsedTriggers.get(0).getOperation(), is(NumberTrigger.Operation.LESS_THAN));
        assertThat(parsedTriggers.get(0).getTrigger(), is(equalTo(12L)));
        assertThat(parsedTriggers.get(1).getOperation(), is(NumberTrigger.Operation.LESS_THAN));
        assertThat(parsedTriggers.get(1).getTrigger(), is(equalTo(23L)));
    }

}