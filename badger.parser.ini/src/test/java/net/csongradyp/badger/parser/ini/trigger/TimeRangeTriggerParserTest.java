package net.csongradyp.badger.parser.ini.trigger;

import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TimeRangeTriggerParserTest {

    @Mock
    private DateProvider mockDateProvider;
    private TimeRangeTriggerParser underTest;

    @Before
    public void setUp() {
        underTest = new TimeRangeTriggerParser(mockDateProvider);
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenGivenTriggerDoesNotHaveARangePair() throws Exception {
        underTest.parse(new String[]{"11:11"});
    }

    @Test
    public void testParseReturnsTimeTriggerPairsRelatedToGivenStartAndEndTimes() throws Exception {
        final String start = "11:11";
        final String end = "22:22";
        final LocalTime startTime = new LocalTime(11, 11);
        final LocalTime endTime = new LocalTime(22, 22);
        given(mockDateProvider.parseTime(start)).willReturn(new LocalTime(11, 11));
        given(mockDateProvider.parseTime(end)).willReturn(new LocalTime(22, 22));

        final List<TimeTriggerPair> result = underTest.parse(new String[]{start, end});

        assertThat(result.isEmpty(), is(false));
        assertThat(result.get(0).getType(), is(AchievementType.TIME_RANGE));
        assertThat(result.get(0).getStartTrigger(), is(startTime));
        assertThat(result.get(0).getEndTrigger(), is(endTime));
    }
}