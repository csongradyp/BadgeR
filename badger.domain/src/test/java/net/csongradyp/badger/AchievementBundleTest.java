package net.csongradyp.badger;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.achievement.*;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AchievementBundleTest {

    private AchievementBundle underTest;

    @Before
    public void setUp() {
        underTest = new AchievementBundle();
    }

    @Test
    public void testSetEventsCreatesInitialContainerForAchievementsFromGivenEventArray() {
        final String event1 = "event1";
        final String event2 = "event2";
        final String[] events = {event1, event2};

        underTest.setEvents(events);

        final Map<String, Set<IAchievement>> allByEvents = underTest.getAllByEvents();
        assertThat(allByEvents.isEmpty(), is(false));
        assertThat(allByEvents.keySet().contains(event1), is(true));
        assertThat(allByEvents.get(event1), is(Collections.<IAchievement>emptySet()));
        assertThat(allByEvents.keySet().contains(event2), is(true));
        assertThat(allByEvents.get(event2), is(Collections.<IAchievement>emptySet()));
    }

    @Test
    public void testSetEventsCreatesInitialContainerForAchievementsFromGivenEventCollection() {
        final String event1 = "event1";
        final String event2 = "event2";
        final String[] events = {event1, event2};

        underTest.setEvents(Arrays.asList(events));

        final Map<String, Set<IAchievement>> allByEvents = underTest.getAllByEvents();
        assertThat(allByEvents.isEmpty(), is(false));
        assertThat(allByEvents.keySet().contains(event1), is(true));
        assertThat(allByEvents.get(event1), is(Collections.<IAchievement>emptySet()));
        assertThat(allByEvents.keySet().contains(event2), is(true));
        assertThat(allByEvents.get(event2), is(Collections.<IAchievement>emptySet()));
    }

    @Test(expected = MalformedAchievementDefinition.class)
    public void testSetAchievementsThrowsExceptionWhenEventDeclarationIsMissingForGivenAchievement() {
        final String event = "event";
        final String[] events = {event};
        underTest.setEvents(events);
        final SingleAchievementBean single = new SingleAchievementBean();
        single.setId("single");
        single.setSubscriptions(new String[]{"notRegistered"});
        final List<IAchievement> achievements = Arrays.asList(single);

        underTest.setAchievements(achievements);
    }

    @Test
    public void testSetAchievements() {
        final String event1 = "event1";
        final String event2 = "event2";
        final String[] events = {event1, event2};
        final SingleAchievementBean single = new SingleAchievementBean();
        single.setId("single");
        single.setCategory("A");
        single.setSubscriptions(new String[]{event1});
        final ScoreAchievementBean score = new ScoreAchievementBean();
        score.setId("score");
        score.setCategory("A");
        score.setSubscriptions(new String[]{event1});
        final DateAchievementBean date = new DateAchievementBean();
        date.setId("date");
        date.setCategory("B");
        date.setSubscriptions(new String[]{event1});
        final TimeAchievementBean time = new TimeAchievementBean();
        time.setId("time");
        time.setCategory("B");
        time.setSubscriptions(new String[]{event2});
        final TimeRangeAchievementBean timeRange = new TimeRangeAchievementBean();
        timeRange.setId("timeRange");
        timeRange.setCategory("B");
        timeRange.setSubscriptions(new String[]{event2});
        final List<IAchievement> achievements = Arrays.asList(single, score, date, time, timeRange);
        underTest.setEvents(events);

        underTest.setAchievements(achievements);

        final Collection<IAchievement> all = underTest.getAll();
        assertThat(all.containsAll(achievements), is(true));
        final Map<String, Set<IAchievement>> allByEvents = underTest.getAllByEvents();
        assertThat(allByEvents.containsKey(event1), is(true));
        assertThat(allByEvents.get(event1).contains(single), is(true));
        assertThat(allByEvents.get(event1).contains(score), is(true));
        assertThat(allByEvents.get(event1).contains(date), is(true));
        assertThat(underTest.getAchievementsSubscribedFor(event1), is(allByEvents.get(event1)));
        assertThat(allByEvents.containsKey(event2), is(true));
        assertThat(allByEvents.get(event2).contains(time), is(true));
        assertThat(allByEvents.get(event2).contains(timeRange), is(true));
        assertThat(underTest.getAchievementsSubscribedFor(event2), is(allByEvents.get(event2)));
        final Collection<IAchievement> forCategoryA = underTest.getAchievementsForCategory("A");
        assertThat(forCategoryA.contains(single), is(true));
        assertThat(forCategoryA.contains(score), is(true));
        final Collection<IAchievement> forCategoryB = underTest.getAchievementsForCategory("B");
        assertThat(forCategoryB.contains(date), is(true));
        assertThat(forCategoryB.contains(time), is(true));
        assertThat(forCategoryB.contains(timeRange), is(true));
        assertThat(underTest.get(AchievementType.SINGLE, "single").get(), is(single));
        assertThat(underTest.get("single").get(), is(single));
        assertThat(underTest.get(AchievementType.SCORE, "score").get(), is(score));
        assertThat(underTest.get("score").get(), is(score));
        assertThat(underTest.get(AchievementType.DATE, "date").get(), is(date));
        assertThat(underTest.get("date").get(), is(date));
        assertThat(underTest.get(AchievementType.TIME, "time").get(), is(time));
        assertThat(underTest.get("time").get(), is(time));
        assertThat(underTest.get(AchievementType.TIME_RANGE, "timeRange").get(), is(timeRange));
        assertThat(underTest.get("timeRange").get(), is(timeRange));
    }
}
