package net.csongradyp.badger.domain.achievement;

import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreAchievementBeanTest {

    private ScoreAchievementBean underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new ScoreAchievementBean();
    }

    @Test
    public void testScoreAchievementBeanWithEqualTrigger() {
        final ArrayList<ScoreTrigger> triggers = new ArrayList<>();
        triggers.add(new ScoreTrigger(10L, ScoreTrigger.Operation.EQUALS));
        triggers.add(new ScoreTrigger(20L, ScoreTrigger.Operation.EQUALS));

        underTest.setTrigger(triggers);
        final List<ScoreTrigger> parsedTriggers = underTest.getTrigger();

        assertThat(parsedTriggers.size(), is(equalTo(2)));
        assertThat(parsedTriggers.get(0).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(parsedTriggers.get(0).getTrigger(), is(equalTo(10L)));
        assertThat(parsedTriggers.get(1).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(parsedTriggers.get(1).getTrigger(), is(equalTo(20L)));
    }

    @Test
    public void testScoreAchievementBeanWithGreaterThanTrigger() {
        final ArrayList<ScoreTrigger> triggers = new ArrayList<>();
        triggers.add(new ScoreTrigger(11L, ScoreTrigger.Operation.GREATER_THAN));
        triggers.add(new ScoreTrigger(22L, ScoreTrigger.Operation.GREATER_THAN));

        underTest.setTrigger(triggers);
        final List<ScoreTrigger> parsedTriggers = underTest.getTrigger();

        assertThat(parsedTriggers.size(), is(equalTo(2)));
        assertThat(parsedTriggers.get(0).getOperation(), is(ScoreTrigger.Operation.GREATER_THAN));
        assertThat(parsedTriggers.get(0).getTrigger(), is(equalTo(11L)));
        assertThat(parsedTriggers.get(1).getOperation(), is(ScoreTrigger.Operation.GREATER_THAN));
        assertThat(parsedTriggers.get(1).getTrigger(), is(equalTo(22L)));
    }

    @Test
    public void testScoreAchievementBeanWithLessThanTrigger() {
        final ArrayList<ScoreTrigger> triggers = new ArrayList<>();
        triggers.add(new ScoreTrigger(12L, ScoreTrigger.Operation.LESS_THAN));
        triggers.add(new ScoreTrigger(23L, ScoreTrigger.Operation.LESS_THAN));

        underTest.setTrigger(triggers);
        final List<ScoreTrigger> parsedTriggers = underTest.getTrigger();

        assertThat(parsedTriggers.size(), is(equalTo(2)));
        assertThat(parsedTriggers.get(0).getOperation(), is(ScoreTrigger.Operation.LESS_THAN));
        assertThat(parsedTriggers.get(0).getTrigger(), is(equalTo(12L)));
        assertThat(parsedTriggers.get(1).getOperation(), is(ScoreTrigger.Operation.LESS_THAN));
        assertThat(parsedTriggers.get(1).getTrigger(), is(equalTo(23L)));
    }

}
