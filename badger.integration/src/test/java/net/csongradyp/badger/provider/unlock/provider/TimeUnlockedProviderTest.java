package net.csongradyp.badger.provider.unlock.provider;

import java.util.Optional;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.provider.date.IDateProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TimeUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    @Mock
    private IDateProvider mockDateProvider;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;

    private TimeUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new TimeUnlockedProvider();
        underTest.setDateProvider(mockDateProvider);
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecision() {
        final String time = "23:14";
        final TimeAchievementBean timeAchievementBean = givenTimeRangeAchievementBean(time);
        given(mockDateProvider.currentTime()).willReturn(time);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", time);
        given(mockUnlockedEventFactory.createEvent(timeAchievementBean, time)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeAchievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecisionAndIsAlreadyUnlocked() {
        final String time = "23:14";
        final TimeAchievementBean timeAchievementBean = givenTimeRangeAchievementBean(time);
        given(mockDateProvider.currentTime()).willReturn(time);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(true);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeAchievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenNoTriggerIsMatchingTheCurrentTime() {
        final TimeAchievementBean timeAchievementBean = givenTimeRangeAchievementBean("23:14");
        given(mockDateProvider.currentTime()).willReturn("23:15");

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeAchievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    private TimeAchievementBean givenTimeRangeAchievementBean(final String trigger) {
        final TimeAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(new String[]{trigger});
        return timeAchievementBean;
    }

}
