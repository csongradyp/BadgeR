package net.csongradyp.badger.parser.api.trigger;

import java.util.Arrays;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreTriggerParserTest {

    private ScoreTriggerParser underTest;

    @Before
    public void setUp() {
        underTest = new ScoreTriggerParser();
    }

    @Test
    public void testParseCreatesTriggersFromIniSourceWithEqualOperation() throws Exception {
        final List<ScoreTrigger> scoreTriggers = underTest.parse(Arrays.asList("1", "42"));

        assertThat(scoreTriggers.size(), is(equalTo(2)));
        assertThat(scoreTriggers.get(0).getType(), is(AchievementType.SCORE));
        assertThat(scoreTriggers.get(0).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(scoreTriggers.get(0).getTrigger(), is(equalTo(1L)));
        assertThat(scoreTriggers.get(1).getType(), is(AchievementType.SCORE));
        assertThat(scoreTriggers.get(1).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(scoreTriggers.get(1).getTrigger(), is(equalTo(42L)));
    }

    @Test
    public void testParseCreatesTriggersFromIniSourceWithGreaterThanOperation() throws Exception {
        final List<ScoreTrigger> scoreTriggers = underTest.parse(Arrays.asList("10+", "42+"));

        assertThat(scoreTriggers.size(), is(equalTo(2)));
        assertThat(scoreTriggers.get(0).getType(), is(AchievementType.SCORE));
        assertThat(scoreTriggers.get(0).getOperation(), is(ScoreTrigger.Operation.GREATER_THAN));
        assertThat(scoreTriggers.get(0).getTrigger(), is(equalTo(10L)));
        assertThat(scoreTriggers.get(1).getType(), is(AchievementType.SCORE));
        assertThat(scoreTriggers.get(1).getOperation(), is(ScoreTrigger.Operation.GREATER_THAN));
        assertThat(scoreTriggers.get(1).getTrigger(), is(equalTo(42L)));
    }

    @Test
    public void testParseCreatesTriggersFromIniSourceWithLessThanOperation() throws Exception {
        final List<ScoreTrigger> scoreTriggers = underTest.parse(Arrays.asList("10-", "42-"));

        assertThat(scoreTriggers.size(), is(equalTo(2)));
        assertThat(scoreTriggers.get(0).getType(), is(AchievementType.SCORE));
        assertThat(scoreTriggers.get(0).getOperation(), is(ScoreTrigger.Operation.LESS_THAN));
        assertThat(scoreTriggers.get(0).getTrigger(), is(equalTo(10L)));
        assertThat(scoreTriggers.get(1).getType(), is(AchievementType.SCORE));
        assertThat(scoreTriggers.get(1).getOperation(), is(ScoreTrigger.Operation.LESS_THAN));
        assertThat(scoreTriggers.get(1).getTrigger(), is(equalTo(42L)));
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenAnyOfTheGivenTriggersAreMalformed() {
        underTest.parse(Arrays.asList("10+-", "-42"));
    }

}