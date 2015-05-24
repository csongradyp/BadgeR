package net.csongradyp.badger.provider.unlock.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ScoreUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    private static final long TEN = 10L;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;

    private ScoreUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new ScoreUnlockedProvider();
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreEqualsTheTriggerDefinedWithEquality() {
        final ScoreAchievementBean scoreAchievementBean = givenScoreAchievementBean(10L, ScoreTrigger.Operation.EQUALS);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(TEN));
        given(mockUnlockedEventFactory.createEvent(scoreAchievementBean, 1, TEN)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(scoreAchievementBean, TEN);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsGreaterThanTheTriggerDefinedWithPlus() {
        final ScoreAchievementBean scoreAchievementBean = givenScoreAchievementBean(5L, ScoreTrigger.Operation.GREATER_THAN);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(TEN));
        given(mockUnlockedEventFactory.createEvent(scoreAchievementBean, 1, TEN)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(scoreAchievementBean, TEN);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsEqualToTheTriggerDefinedWithPlus() {
        final ScoreAchievementBean counterAchievementBean = givenScoreAchievementBean(10L, ScoreTrigger.Operation.GREATER_THAN);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(TEN));
        given(mockUnlockedEventFactory.createEvent(counterAchievementBean, 1, TEN)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, TEN);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsLessThanTheTriggerDefinedWithMinus() {
        final ScoreAchievementBean scoreAchievementBean = givenScoreAchievementBean(20L, ScoreTrigger.Operation.LESS_THAN);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(TEN));
        given(mockUnlockedEventFactory.createEvent(scoreAchievementBean, 1, TEN)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(scoreAchievementBean, TEN);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsEqualToTheTriggerDefinedWithMinus() {
        final ScoreAchievementBean scoreAchievementBean = givenScoreAchievementBean(10L, ScoreTrigger.Operation.LESS_THAN);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(TEN));
        given(mockUnlockedEventFactory.createEvent(scoreAchievementBean, 1, TEN)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(scoreAchievementBean, TEN);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTheGivenScoreIsGreaterThanTheTriggerDefinedWithMinus() {
        final ScoreAchievementBean scoreAchievementBean = givenScoreAchievementBean(9L, ScoreTrigger.Operation.LESS_THAN);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(scoreAchievementBean, TEN);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTheGivenScoreIsLessThanTheTriggerDefinedWithPlus() {
        final ScoreAchievementBean scoreAchievementBean = givenScoreAchievementBean(11L, ScoreTrigger.Operation.GREATER_THAN);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(scoreAchievementBean, TEN);

        assertThat(result.isPresent(), is(false));
    }

    private ScoreAchievementBean givenScoreAchievementBean(final Long trigger, ScoreTrigger.Operation operation) {
        final ScoreAchievementBean timeAchievementBean = new ScoreAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(givenScoreTrigger(trigger, operation));
        return timeAchievementBean;
    }

    private List<ScoreTrigger> givenScoreTrigger(final Long score, ScoreTrigger.Operation operation) {
        List<ScoreTrigger> dateTriggers = new ArrayList<>();
        final ScoreTrigger trigger = new ScoreTrigger(score, operation);
        dateTriggers.add(trigger);
        return dateTriggers;
    }
}
