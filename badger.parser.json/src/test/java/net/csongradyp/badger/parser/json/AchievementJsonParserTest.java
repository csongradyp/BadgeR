package net.csongradyp.badger.parser.json;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.parser.api.RelationParser;
import net.csongradyp.badger.parser.api.RelationValidator;
import net.csongradyp.badger.parser.api.trigger.DateTriggerParser;
import net.csongradyp.badger.parser.api.trigger.ITriggerParser;
import net.csongradyp.badger.parser.api.trigger.ScoreTriggerParser;
import net.csongradyp.badger.parser.api.trigger.TimeTriggerParser;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AchievementJsonParserTest {

    private static final String JSON_FILE = "test.json";
    @Mock
    private Map<AchievementType, ITriggerParser> mockJsonTriggerParsers;
    private ScoreTriggerParser scoreTriggerParser;
    private TimeTriggerParser timeTriggerParser;
    private DateTriggerParser dateTriggerParser;

    private AchievementJsonParser underTest;

    @Before
    public void setUp() {
        final DateProvider dateProvider = new DateProvider();
        timeTriggerParser = new TimeTriggerParser(dateProvider);
        dateTriggerParser = new DateTriggerParser(dateProvider);
        scoreTriggerParser = new ScoreTriggerParser();

        underTest = new AchievementJsonParser();
        underTest.setJsonTriggerParsers(mockJsonTriggerParsers);
        underTest.setDateProvider(new DateProvider());
        final RelationParser relationParser = new RelationParser();
        relationParser.setRelationValidator(new RelationValidator());
        underTest.setRelationParser(relationParser);

        given(mockJsonTriggerParsers.get(AchievementType.SCORE)).willReturn(scoreTriggerParser);
        given(mockJsonTriggerParsers.get(AchievementType.TIME)).willReturn(timeTriggerParser);
        given(mockJsonTriggerParsers.get(AchievementType.DATE)).willReturn(dateTriggerParser);
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenInvalidURLIsGiven() throws Exception {
        final URL achievementFile = new URL("file", "localhost", "invalid.json");
        underTest.parse(achievementFile);
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenInvalidFilePathIsGiven() throws Exception {
        underTest.parse("invalid.json");
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testParseThrowsExceptionWhenInvalidFileIsGiven() throws Exception {
        final File achievementFile = new File("");
        underTest.parse(achievementFile);
    }

    @Test
    public void testParseFromFilePath() throws IOException {
        final String fileLocation = getClass().getClassLoader().getResource(JSON_FILE).getFile();
        final AchievementDefinition result = underTest.parse(fileLocation);
        assertResult(result);
    }

    @Test
    public void testParseFromFile() {
        final URL jsonFile = getClass().getClassLoader().getResource(JSON_FILE);
        final AchievementDefinition result = underTest.parse(new File(jsonFile.getFile()));
        assertResult(result);
    }

    @Test
    public void testParseFromUrl() {
        final URL jsonFile = getClass().getClassLoader().getResource(JSON_FILE);
        final AchievementDefinition result = underTest.parse(jsonFile);
        assertResult(result);
    }

    private void assertResult(AchievementDefinition result) {
        assertThat(result.getAll().size(), is(equalTo(7)));
        assertThat(result.getAllByEvents().keySet().size(), is(equalTo(3)));
        assertCategories(result);
        assertAchievements(result);
    }

    private void assertCategories(final AchievementDefinition result) {
        assertThat(result.getAchievementsForCategory("manual").size(), is(equalTo(1)));
        assertThat(result.getAchievementsForCategory("default").size(), is(equalTo(5)));
        assertThat(result.getAchievementsForCategory("complex").size(), is(equalTo(1)));
    }

    private void assertAchievements(final AchievementDefinition result) {
        Optional<IAchievement> achievement;
        achievement = result.get("simple");
        assertThat(achievement.isPresent(), is(true));
        final IAchievement single = achievement.get();
        assertThat(single.getType(), is(AchievementType.SINGLE));
        assertThat(single.getCategory(), is("manual"));
        assertThat(single.getEvent().size(), is(equalTo(1)));
        assertThat(single.getEvent().get(0), is(equalTo("sample")));

        achievement = result.get("first");
        assertThat(achievement.isPresent(), is(true));
        final ITriggerableAchievementBean<ScoreTrigger> score = (ITriggerableAchievementBean<ScoreTrigger>) achievement.get();
        assertThat(score.getType(), is(AchievementType.SCORE));
        assertThat(score.getCategory(), is("default"));
        assertThat(score.getEvent().size(), is(equalTo(2)));
        assertThat(score.getEvent().get(0), is(equalTo("sample")));
        assertThat(score.getEvent().get(1), is(equalTo("sample3")));
        assertThat(score.getTrigger().size(), is(equalTo(4)));
        assertThat(score.getTrigger().get(0).getTrigger(), is(equalTo(1L)));
        assertThat(score.getTrigger().get(0).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(score.getTrigger().get(1).getTrigger(), is(equalTo(2L)));
        assertThat(score.getTrigger().get(1).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(score.getTrigger().get(2).getTrigger(), is(equalTo(3L)));
        assertThat(score.getTrigger().get(2).getOperation(), is(ScoreTrigger.Operation.EQUALS));
        assertThat(score.getTrigger().get(3).getTrigger(), is(equalTo(4L)));
        assertThat(score.getTrigger().get(3).getOperation(), is(ScoreTrigger.Operation.EQUALS));

        achievement = result.get("scoreRange-test");
        assertThat(achievement.isPresent(), is(true));
        final ITriggerableAchievementBean<ScoreTriggerPair> scoreRange = (ITriggerableAchievementBean<ScoreTriggerPair>) achievement.get();
        assertThat(scoreRange.getType(), is(AchievementType.SCORE_RANGE));
        assertThat(scoreRange.getCategory(), is("default"));
        assertThat(scoreRange.getEvent().size(), is(equalTo(2)));
        assertThat(scoreRange.getEvent().get(0), is(equalTo("sample")));
        assertThat(scoreRange.getEvent().get(1), is(equalTo("sample2")));
        assertThat(scoreRange.getTrigger().size(), is(equalTo(2)));
        assertThat(scoreRange.getTrigger().get(0).getStartTrigger(), is(equalTo(0L)));
        assertThat(scoreRange.getTrigger().get(0).getEndTrigger(), is(equalTo(10L)));
        assertThat(scoreRange.getTrigger().get(1).getStartTrigger(), is(equalTo(50L)));
        assertThat(scoreRange.getTrigger().get(1).getEndTrigger(), is(equalTo(20L)));

        achievement = result.get("timeRange-test");
        assertThat(achievement.isPresent(), is(true));
        final ITriggerableAchievementBean<TimeTriggerPair> timeRange = (ITriggerableAchievementBean<TimeTriggerPair>) achievement.get();
        assertThat(timeRange.getType(), is(AchievementType.TIME_RANGE));
        assertThat(timeRange.getCategory(), is("default"));
        assertThat(timeRange.getEvent().size(), is(equalTo(1)));
        assertThat(timeRange.getEvent().get(0), is(equalTo("sample3")));
        assertThat(timeRange.getTrigger().size(), is(equalTo(1)));
        assertThat(timeRange.getTrigger().get(0).getStartTrigger(), is(equalTo(new LocalTime(0, 10))));
        assertThat(timeRange.getTrigger().get(0).getEndTrigger(), is(equalTo(new LocalTime(2, 0))));

        achievement = result.get("time-test");
        assertThat(achievement.isPresent(), is(true));
        final ITriggerableAchievementBean<TimeTrigger> time = (ITriggerableAchievementBean<TimeTrigger>) achievement.get();
        assertThat(time.getType(), is(AchievementType.TIME));
        assertThat(time.getCategory(), is("default"));
        assertThat(time.getEvent().size(), is(equalTo(2)));
        assertThat(time.getEvent().get(0), is(equalTo("sample2")));
        assertThat(time.getEvent().get(1), is(equalTo("sample3")));
        assertThat(time.getTrigger().size(), is(equalTo(2)));
        assertThat(time.getTrigger().get(0).getTime(), is(equalTo(new LocalTime(11, 11))));
        assertThat(time.getTrigger().get(1).getTime(), is(equalTo(new LocalTime(12, 12))));

        achievement = result.get("date-test");
        assertThat(achievement.isPresent(), is(true));
        final ITriggerableAchievementBean<DateTrigger> date = (ITriggerableAchievementBean<DateTrigger>) achievement.get();
        assertThat(date.getType(), is(AchievementType.DATE));
        assertThat(date.getCategory(), is("default"));
        assertThat(date.getEvent().size(), is(equalTo(1)));
        assertThat(date.getEvent().get(0), is(equalTo("sample2")));
        assertThat(date.getTrigger().size(), is(equalTo(2)));
        assertThat(date.getTrigger().get(0).getDate(), is(equalTo(new DateTime(2000, 12, 12, 0, 0).toDate())));
        assertThat(date.getTrigger().get(1).getDate(), is(equalTo(new DateTime(2000, 5, 22, 0, 0).toDate())));

        achievement = result.get("composite-test");
        assertThat(achievement.isPresent(), is(true));
        final CompositeAchievementBean composite = (CompositeAchievementBean) achievement.get();
        assertThat(composite.getType(), is(AchievementType.COMPOSITE));
        assertThat(composite.getCategory(), is("complex"));
        assertThat(composite.getEvent().size(), is(equalTo(1)));
        assertThat(composite.getEvent().get(0), is(equalTo("sample2")));
        assertThat(composite.getTrigger().size(), is(equalTo(10)));
    }
}
