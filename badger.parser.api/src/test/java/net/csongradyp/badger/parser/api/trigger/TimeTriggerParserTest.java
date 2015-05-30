package net.csongradyp.badger.parser.api.trigger;

import java.util.Arrays;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.parser.api.trigger.TimeTriggerParser;
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
public class TimeTriggerParserTest {

    private TimeTriggerParser underTest;
    @Mock
    private DateProvider mockDateProvider;

    @Before
    public void setUp() {
        underTest = new TimeTriggerParser(mockDateProvider);
    }

    @Test
    public void testParseCreatesTriggersFromIniSource() throws Exception {
        final String firstTimeString = "01:11";
        final String secondTimeString = "02:22";
        final LocalTime firstDate = new LocalTime(1, 11);
        final LocalTime secondDate = new LocalTime(2, 22);
        final List<String> iniTrigger = Arrays.asList(firstTimeString, secondTimeString);
        given(mockDateProvider.parseTime(firstTimeString)).willReturn(firstDate);
        given(mockDateProvider.parseTime(secondTimeString)).willReturn(secondDate);

        final List<TimeTrigger> result = underTest.parse(iniTrigger);

        assertThat(result.isEmpty(), is(false));
        assertThat(result.get(0).getType(), is(AchievementType.TIME));
        assertThat(result.get(0).getTime(), is(firstDate));
        assertThat(result.get(1).getType(), is(AchievementType.TIME));
        assertThat(result.get(1).getTime(), is(secondDate));

    }
}