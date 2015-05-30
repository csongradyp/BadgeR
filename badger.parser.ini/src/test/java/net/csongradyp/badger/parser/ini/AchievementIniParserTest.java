package net.csongradyp.badger.parser.ini;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.parser.api.RelationParser;
import net.csongradyp.badger.parser.api.trigger.ITriggerParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AchievementIniParserTest {

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private RelationParser mockRelationParser;
    @Mock
    private Map<AchievementType, ITriggerParser> mockTriggerParsers;
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private ITriggerParser mockTriggerParser;

    private AchievementIniParser underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new AchievementIniParser();
        underTest.setTriggerParsers(mockTriggerParsers);
        underTest.setRelationParser(mockRelationParser);
        when(mockTriggerParsers.get(any())).thenReturn(mockTriggerParser);
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenInvalidURLIsGiven() throws Exception {
        final URL achievementFile = new URL("file", "localhost", "invalid.ini");
        underTest.parse(achievementFile);
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenInvalidFilePathIsGiven() throws Exception {
        underTest.parse("invalid.ini");
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenInvalidFileIsGiven() throws Exception {
        final File achievementFile = new File("");
        underTest.parse(achievementFile);
    }

    @Test
    public void testParseReturnsAchievementDefinitionWhenIniFileURLIsGiven() throws Exception {
        final URL achievementFile = getClass().getClassLoader().getResource("test.ini");
        try {
            underTest.parse(achievementFile);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseReturnsAchievementDefinitionWhenIniFilePathIsGiven() throws Exception {
        final String achievementFile = getClass().getClassLoader().getResource("test.ini").getPath();
        try {
            underTest.parse(achievementFile);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseReturnsAchievementDefinitionWhenIniFileIsGiven() throws Exception {
        final File achievementFile = new File(getClass().getClassLoader().getResource("test.ini").getPath());

        final AchievementDefinition result = underTest.parse(achievementFile);

        verify(mockRelationParser, times(2)).parse(anyString(), anyCollection());
        final Optional<IAchievement> simple = result.get(AchievementType.SINGLE, "simple");
        assertThat(simple.isPresent(), is(true));
        assertThat(simple.get().getEvent(), is(equalTo(Collections.<String>emptyList())));
        assertThat(simple.get().getCategory(), is("default"));
        assertThat(result.get("simple"), is(simple));
        final Optional<IAchievement> first1 = result.get(AchievementType.SCORE, "first");
        assertThat(first1.isPresent(), is(true));
        final IAchievement first = result.get("first").get();
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("3", "7"));
        assertThat(first, notNullValue());
        assertThat(first.getType(), is(AchievementType.SCORE));
        assertThat(first.getId(), is("first"));
        assertThat(first.getEvent().size(), is(equalTo(1)));
        assertThat(first.getEvent().get(0), is("sample"));
        assertThat(first.getCategory(), is("something"));
        final IAchievement date = result.get("date-test").get();
        assertThat(date, notNullValue());
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("12-30"));
        assertThat(date.getType(), is(AchievementType.DATE));
        assertThat(date.getCategory(), is("default"));
        assertThat(date.getEvent().size(), is(equalTo(1)));
        assertThat(date.getEvent().get(0), is("sample"));
        final IAchievement time = result.get("time-test").get();
        assertThat(time, notNullValue());
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("08:15"));
        assertThat(time.getType(), is(AchievementType.TIME));
        assertThat(time.getCategory(), is("default"));
        assertThat(time.getEvent().size(), is(equalTo(1)));
        assertThat(time.getEvent().get(0), is("sample"));
        final IAchievement timeRange = result.get("time-range-test").get();
        assertThat(timeRange, notNullValue());
        assertThat(timeRange.getType(), is(AchievementType.TIME_RANGE));
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("01:30", "02:00"));
        assertThat(timeRange.getCategory(), is("default"));
        assertThat(timeRange.getEvent().size(), is(equalTo(1)));
        assertThat(timeRange.getEvent().get(0), is("sample2"));
        final IAchievement composite1 = result.get("composite-dateTime").get();
        assertThat(composite1, notNullValue());
        assertThat(composite1.getType(), is(AchievementType.COMPOSITE));
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("04:00", "05:00"));
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("05-04", "06-22"));
        assertThat(composite1.getCategory(), is("default"));
        assertThat(composite1.getEvent().size(), is(equalTo(1)));
        assertThat(composite1.getEvent().get(0), is("sample2"));
        final IAchievement composite2 = result.get("composite-dateTime").get();
        assertThat(composite2, notNullValue());
        assertThat(composite2.getType(), is(AchievementType.COMPOSITE));
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("01-23"));
        verify(mockTriggerParser, times(1)).parse(Arrays.asList("100+"));
        assertThat(composite2.getCategory(), is("default"));
        assertThat(composite2.getEvent().size(), is(equalTo(1)));
        assertThat(composite2.getEvent().get(0), is("sample2"));
    }
}
