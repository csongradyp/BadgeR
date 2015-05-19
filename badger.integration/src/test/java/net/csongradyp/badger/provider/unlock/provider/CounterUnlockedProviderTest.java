package net.csongradyp.badger.provider.unlock.provider;

import java.util.Optional;
import net.csongradyp.badger.domain.achievement.CounterAchievementBean;
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
public class CounterUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    public static final long GIVEN_SCORE = 10L;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;

    private CounterUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new CounterUnlockedProvider();
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreEqualsTheTriggerDefinedWithEquality() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("10");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(GIVEN_SCORE));
        given(mockUnlockedEventFactory.createEvent(counterAchievementBean, 1, GIVEN_SCORE)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsGreaterThanTheTriggerDefinedWithPlus() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("5+");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(GIVEN_SCORE));
        given(mockUnlockedEventFactory.createEvent(counterAchievementBean, 1, GIVEN_SCORE)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsEqualToTheTriggerDefinedWithPlus() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("10+");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(GIVEN_SCORE));
        given(mockUnlockedEventFactory.createEvent(counterAchievementBean, 1, GIVEN_SCORE)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsLessThanTheTriggerDefinedWithMinus() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("20-");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(GIVEN_SCORE));
        given(mockUnlockedEventFactory.createEvent(counterAchievementBean, 1, GIVEN_SCORE)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsUnlockableAchievementWhenTheGivenScoreIsEqualToTheTriggerDefinedWithMinus() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("10-");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", String.valueOf(GIVEN_SCORE));
        given(mockUnlockedEventFactory.createEvent(counterAchievementBean, 1, GIVEN_SCORE)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTheGivenScoreIsGreaterThanTheTriggerDefinedWithMinus() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("9-");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTheGivenScoreIsLessThanTheTriggerDefinedWithPlus() {
        final CounterAchievementBean counterAchievementBean = givenScoreAchievementBean("11+");
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(counterAchievementBean, GIVEN_SCORE);

        assertThat(result.isPresent(), is(false));
    }

    private CounterAchievementBean givenScoreAchievementBean(final String trigger) {
        final CounterAchievementBean timeAchievementBean = new CounterAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(new String[]{trigger});
        return timeAchievementBean;
    }
}
