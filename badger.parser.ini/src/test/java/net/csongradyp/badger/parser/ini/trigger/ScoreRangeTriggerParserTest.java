package net.csongradyp.badger.parser.ini.trigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScoreRangeTriggerParserTest {

    private ScoreRangeTriggerParser underTest;

    @Before
    public void setUp() {
        underTest = new ScoreRangeTriggerParser();
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenGivenTriggerDoesNotHaveARangePair() throws Exception {
        underTest.parse(Collections.singletonList("10"));
    }

    @Test
    public void testParseReturnsScoreTriggerPairsRelatedToGivenStartAndEndScores() throws Exception {
        final List<String> triggers = Arrays.asList("0", "10", "20", "30");

        final List<ScoreTriggerPair> result = underTest.parse(triggers);

        assertThat(result.size(), is(equalTo(2)));
        assertThat(result.get(0).getType(), is(AchievementType.SCORE_RANGE));
        assertThat(result.get(0).getStartTrigger(), is(0L));
        assertThat(result.get(0).getEndTrigger(), is(10L));
        assertThat(result.get(1).getStartTrigger(), is(20L));
        assertThat(result.get(1).getEndTrigger(), is(30L));
    }
}