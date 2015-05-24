package net.csongradyp.badger.parser.ini.trigger;

import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DateTriggerParserTest {

    private DateTriggerParser underTest;
    @Mock
    private DateProvider mockDateProvider;

    @Before
    public void setUp() {
        underTest = new DateTriggerParser(mockDateProvider);
    }

    @Test
    public void testParseCreatesTriggersFromIniSource() throws Exception {
        final String firstDateString = "01-11";
        final String secondDateString = "02-22";
        final Date firstDate = new DateTime(2000, 1, 11, 0, 0).toDate();
        final Date secondDate = new DateTime(2000, 2, 22, 0, 0).toDate();
        final String[] iniTrigger = {firstDateString, secondDateString};
        given(mockDateProvider.parseDate(firstDateString)).willReturn(firstDate);
        given(mockDateProvider.parseDate(secondDateString)).willReturn(secondDate);

        final List<DateTrigger> result = underTest.parse(iniTrigger);

        assertThat(result.isEmpty(), is(false));
        assertThat(result.get(0).getType(), is(AchievementType.DATE));
        assertThat(result.get(0).getDate(), is(firstDate));
        assertThat(result.get(1).getType(), is(AchievementType.DATE));
        assertThat(result.get(1).getDate(), is(secondDate));

    }
}