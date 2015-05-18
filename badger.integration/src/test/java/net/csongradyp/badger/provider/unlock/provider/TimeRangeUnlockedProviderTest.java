package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
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

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TimeRangeUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    @Mock
    private IDateProvider mockDateProvider;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;

    private TimeRangeUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new TimeRangeUnlockedProvider();
        underTest.setDateProvider(mockDateProvider);
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenCurrentTimeIsNotWithinTheGivenRange() throws Exception {
        final TimeRangeAchievementBean timeRangeAchievementBean = givenTimeAchievementBean("08:00", "09:00");
        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(false);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(true);
        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result.isPresent(), is(false));

        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(true);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(false);
        final Optional<IAchievementUnlockedEvent> result2 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result2.isPresent(), is(false));

        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(true);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(false);
        final Optional<IAchievementUnlockedEvent> result3 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result3.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenCurrentTimeIsNotWithinTheReversedGivenRange() {
        final TimeRangeAchievementBean timeRangeAchievementBean = givenTimeAchievementBean("09:00", "09:00");
        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(false);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(true);
        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result.isPresent(), is(false));

        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(true);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(false);
        final Optional<IAchievementUnlockedEvent> result2 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result2.isPresent(), is(false));

        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(true);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(false);
        final Optional<IAchievementUnlockedEvent> result3 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result3.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsnlockableAchievementWhenWhenCurrentTimeIsWithinTheGivenRange() throws Exception {
        final TimeRangeAchievementBean timeRangeAchievementBean = givenTimeAchievementBean("08:00", "09:00");
        final String currentTime = "08:31";
        given(mockDateProvider.currentTime()).willReturn(currentTime);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(true);
        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(true);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", currentTime);
        given(mockUnlockedEventFactory.createEvent(timeRangeAchievementBean, currentTime)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsnlockableAchievementWhenWhenCurrentTimeIsWithinReversedGivenRange() throws Exception {
        final TimeRangeAchievementBean timeRangeAchievementBean = givenTimeAchievementBean("09:00", "08:00");
        final String currentTime = "11:00";
        given(mockDateProvider.currentTime()).willReturn(currentTime);
        given(mockDateProvider.isCurrentTimeAfter(any(Date.class))).willReturn(true);
        given(mockDateProvider.isCurrentTimeBefore(any(Date.class))).willReturn(true);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", currentTime);
        given(mockUnlockedEventFactory.createEvent(timeRangeAchievementBean, currentTime)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    private TimeRangeAchievementBean givenTimeAchievementBean(final String start, final String end) {
        final TimeRangeAchievementBean timeAchievementBean = new TimeRangeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(new String[]{start, end});
        return timeAchievementBean;
    }

}
