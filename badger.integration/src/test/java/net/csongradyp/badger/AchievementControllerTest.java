package net.csongradyp.badger;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.event.AchievementEventType;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.parser.IAchievementDefinitionFileParser;
import net.csongradyp.badger.domain.TestCounterAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.Achievement;
import net.csongradyp.badger.event.message.Score;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.persistence.EventDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AchievementControllerTest {

    private static final String ACHIEVEMENT_ID = "test";

    @Mock
    private IAchievementDefinitionFileParser mockAchievementParser;
    @Mock
    private AchievementDefinition mockAchievementDefinition;
    @Mock
    private AchievementDao mockAchievementDao;
    @Mock
    private EventDao mockEventDao;
    @Mock
    private DateProvider mockDateProvider;

    private ScoreUpdateHandlerWrapper scoreHandler;
    private Score scoreEvent;

    private AchievementController underTest;

    @Before
    public void setUp() {
        underTest = new AchievementController();
        underTest.setDefinition(mockAchievementDefinition);
        underTest.setAchievementDao(mockAchievementDao);
        underTest.setEventDao(mockEventDao);
        underTest.setDateProvider(mockDateProvider);

        scoreHandler = new ScoreUpdateHandlerWrapper(score -> scoreEvent = score);
        EventBus.subscribeOnScoreChanged(scoreHandler);
        scoreEvent = null;
    }

    @After
    public void tearDown() {
        EventBus.unSubscribeOnScoreChanged(scoreHandler.getWrapped());
    }

    @Test
    public void testSetInternationalizationBaseNameSetsResourceBundleWhenInternationalizationFilesArePresentWithTheGivenBaseName() {
        final String baseName = "msg";

        underTest.setInternationalizationBaseName(baseName);

        assertThat(underTest.getResourceBundle(), notNullValue());
        assertThat(underTest.getResourceBundle().getBaseBundleName(), is(equalTo(baseName)));
    }

    @Test(expected = MissingResourceException.class)
    public void testSetInternationalizationBaseNameThrowsExceptionWhenInternationalizationFilesAreNotPresentWithTheGivenBaseName() {
        underTest.setInternationalizationBaseName("wrong name");
    }

    @Test
    public void testGetAllReturnsAllAchievementsFromAchievementDefinition() {
        final ArrayList<IAchievement> achievements = new ArrayList<>();
        given(mockAchievementDefinition.getAll()).willReturn(achievements);

        final Collection<IAchievement> result = underTest.getAll();

        assertThat(result, notNullValue());
        assertThat(result, is(achievements));
        assertThat(result.size(), is(equalTo(achievements.size())));
    }

    @Test
    public void testGetReturnsAchievementsFromAchievementDefinition() {
        final TestCounterAchievementBean testAchievement = new TestCounterAchievementBean(ACHIEVEMENT_ID);
        final AchievementType type = AchievementType.COUNTER;
        given(mockAchievementDefinition.get(type, ACHIEVEMENT_ID)).willReturn(testAchievement);

        final IAchievement result = underTest.get(type, ACHIEVEMENT_ID);

        assertThat(result, notNullValue());
        assertThat(result, is(testAchievement));
    }

    @Test
    public void testGetAllCallUnderlyingBundle() {
        final TestCounterAchievementBean testAchievementBean = new TestCounterAchievementBean(ACHIEVEMENT_ID);
        final ArrayList<IAchievement> parsedAchievements = new ArrayList<>();
        parsedAchievements.add(testAchievementBean);
        when(mockAchievementDefinition.getAll()).thenReturn(parsedAchievements);

        final Collection<IAchievement> achievements = underTest.getAll();

        verify(mockAchievementDefinition).getAll();
        assertThat(achievements.size(), is(equalTo(1)));
        assertThat(achievements.iterator().next(), is(testAchievementBean));
    }

    @Test
    public void testGetAllByEventsReturnsAchievementDefinitionProvidedCollection() {
        final HashMap<String, Set<IAchievement>> achievements = new HashMap<>();
        given(mockAchievementDefinition.getAllByEvents()).willReturn(achievements);

        final Map<String, Set<IAchievement>> result = underTest.getAllByEvents();

        assertThat(achievements, is(result));
    }

    @Test
    public void testCheckAll() {
        // TODO
    }

    @Test
    public void testTriggerEventWithHighScoreDoesNotSetNewScoreANorPublishScoreChangedeventWhenGivenScoreIsLessThanTheCurrentlyStored() {
        final String event = "test";
        final long score = 10L;
        given(mockEventDao.scoreOf(event)).willReturn(11L);

        underTest.triggerEventWithHighScore(event, score);

        verify(mockEventDao, never()).setScore(event, score);
        assertThat(scoreEvent, is(nullValue()));
    }

    @Test
    public void testTriggerEventWithHighScoreDoesNotTriggerEventWhenGivenScoreIsEqualToTheCurrentlyStored() {
        final String event = "test";
        final long score = 10L;
        given(mockEventDao.scoreOf(event)).willReturn(score);

        underTest.triggerEventWithHighScore(event, score);

        verify(mockEventDao, never()).setScore(event, score);
        assertThat(scoreEvent, is(nullValue()));
    }

    @Test
    public void testTriggerEventWithHighScore() {
        final String event = "test";
        final long score = 10L;
        given(mockEventDao.scoreOf(event)).willReturn(9L);
        given(mockEventDao.setScore(event, score)).willReturn(score);

        underTest.triggerEventWithHighScore(event, score);

        verify(mockEventDao).setScore(event, score);
        assertThat(scoreEvent, is(notNullValue()));
        assertThat(scoreEvent.getEvent(), is(event));
        assertThat(scoreEvent.getValue(), is(score));
    }

    @Test
    public void testUnlockPublishUnlockedEventWithProperData() {
        final String triggerValue = "value";
        final AchievementUnlockedHandlerWrapper handler = new AchievementUnlockedHandlerWrapper(achievement -> {
            verifyUnlocalizedAchievement(achievement, triggerValue, Collections.emptySet());
        });
        when(mockAchievementDefinition.get(ACHIEVEMENT_ID)).thenReturn(Optional.of(new TestCounterAchievementBean(ACHIEVEMENT_ID)));
        when(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID, 1)).thenReturn(false);
        EventBus.subscribeOnUnlock(handler);

        underTest.unlock(ACHIEVEMENT_ID, triggerValue);

        verify(mockAchievementDefinition).get(ACHIEVEMENT_ID);
        verify(mockAchievementDao).isUnlocked(ACHIEVEMENT_ID, 1);
        verify(mockAchievementDao).unlock(ACHIEVEMENT_ID, 1, Collections.emptySet());
        EventBus.unSubscribeOnUnlock(handler.getWrapped());
    }
    @Test

    public void testUnlockWithGivenOwnersPublishUnlockedEventWithProperData() {
        final String triggerValue = "value";
        final Set<String> owners = new HashSet<>();
        owners.add("owner");
        final AchievementUnlockedHandlerWrapper handler = new AchievementUnlockedHandlerWrapper(achievement -> {
            verifyUnlocalizedAchievement(achievement, triggerValue, owners);
        });
        when(mockAchievementDefinition.get(ACHIEVEMENT_ID)).thenReturn(Optional.of(new TestCounterAchievementBean(ACHIEVEMENT_ID)));
        when(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID, 1)).thenReturn(false);
        EventBus.subscribeOnUnlock(handler);

        underTest.unlock(ACHIEVEMENT_ID, triggerValue, owners);

        verify(mockAchievementDefinition).get(ACHIEVEMENT_ID);
        verify(mockAchievementDao).isUnlocked(ACHIEVEMENT_ID, 1);
        verify(mockAchievementDao).unlock(ACHIEVEMENT_ID, 1, owners);
        EventBus.unSubscribeOnUnlock(handler.getWrapped());
    }

    @Test
    public void testIsUnlockedCallsUnderlyingDao() {
        underTest.isUnlocked(ACHIEVEMENT_ID);
        verify(mockAchievementDao).isUnlocked(ACHIEVEMENT_ID);
    }

    @Test
    public void testIsUnlockedCallsUnderlyingDaoWithGivenLevel() {
        Integer level = 3;
        underTest.isUnlocked(ACHIEVEMENT_ID, level);
        verify(mockAchievementDao).isUnlocked(ACHIEVEMENT_ID, level);
    }

    @Test
    public void testGetCurrentScoreCallsUnderlyingDao() {
        underTest.getCurrentScore(ACHIEVEMENT_ID);
        verify(mockEventDao).scoreOf(ACHIEVEMENT_ID);
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenGivenDateAchievementTriggerIsEqualToTheCurrentDate() {
        final String date = "02-14";
        final IAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ACHIEVEMENT_ID);
        dateAchievementBean.setTrigger(new String[]{date});
        given(mockDateProvider.currentDate()).willReturn(date);

        final Optional<IAchievementUnlockedEvent> result = underTest.unlockable(0L, dateAchievementBean);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(equalTo(ACHIEVEMENT_ID)));
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecision() {
        final String time = "23:14";
        final IAchievementBean timeAchievementBean = givenTimeAchievementBean(time);
        given(mockDateProvider.currentTime()).willReturn(time);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.unlockable(0L, timeAchievementBean);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(equalTo(ACHIEVEMENT_ID)));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecisionAndIsAlreadyUnlocked() {
        final String time = "23:14";
        final IAchievementBean timeAchievementBean = givenTimeAchievementBean(time);
        given(mockDateProvider.currentTime()).willReturn(time);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(true);

        final Optional<IAchievementUnlockedEvent> result = underTest.unlockable(0L, timeAchievementBean);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenNoTriggerIsMatchingTheCurrentTime() {
        final IAchievementBean timeAchievementBean = givenTimeAchievementBean("23:14");
        given(mockDateProvider.currentTime()).willReturn("23:15");

        final Optional<IAchievementUnlockedEvent> result = underTest.unlockable(0L, timeAchievementBean);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testResetCallsDaosDeleteAllMethods() {
        underTest.reset();

        verify(mockAchievementDao).deleteAll();
        verify(mockEventDao).deleteAll();
    }

    private IAchievementBean givenTimeAchievementBean(final String trigger) {
        final IAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(new String[]{trigger});
        return timeAchievementBean;
    }

    private void verifyUnlocalizedAchievement(final Achievement achievement, final String triggerValue, Set<String> owners) {
        assertThat(achievement.getId(), is(equalTo(ACHIEVEMENT_ID)));
        assertThat(achievement.getTitle(), is(equalTo(ACHIEVEMENT_ID + ".title")));
        assertThat(achievement.getText(), is(equalTo(ACHIEVEMENT_ID + ".text")));
        assertThat(achievement.getLevel(), is(equalTo(1)));
        assertThat(achievement.getEventType(), is(equalTo(AchievementEventType.UNLOCK)));
        assertThat(achievement.getCategory(), is(equalTo("default")));
        assertThat(achievement.getOwners(), is(equalTo(owners)));
        assertThat(achievement.getTriggerValue(), is(equalTo(triggerValue)));
    }

}
