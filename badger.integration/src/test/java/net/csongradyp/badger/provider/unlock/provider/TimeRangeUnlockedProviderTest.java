package net.csongradyp.badger.provider.unlock.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.provider.date.IDateProvider;
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
        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result.isPresent(), is(false));

        final Optional<IAchievementUnlockedEvent> result2 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result2.isPresent(), is(false));

        final Optional<IAchievementUnlockedEvent> result3 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result3.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenCurrentTimeIsNotWithinTheReversedGivenRange() {
        final TimeRangeAchievementBean timeRangeAchievementBean = givenTimeAchievementBean("09:00", "09:02");
        given(mockDateProvider.currentTime()).willReturn(new LocalTime(8, 31).toDateTimeToday().toDate());
        given(mockDateProvider.currentTimeString()).willReturn("");
        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result.isPresent(), is(false));

        given(mockDateProvider.currentTime()).willReturn(new LocalTime(9, 31).toDateTimeToday().toDate());
        given(mockDateProvider.currentTimeString()).willReturn("");
        final Optional<IAchievementUnlockedEvent> result2 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result2.isPresent(), is(false));

        given(mockDateProvider.currentTime()).willReturn(new LocalTime(22, 31).toDateTimeToday().toDate());
        given(mockDateProvider.currentTimeString()).willReturn("");
        final Optional<IAchievementUnlockedEvent> result3 = underTest.getUnlockable(timeRangeAchievementBean, 0L);
        assertThat(result3.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsnlockableAchievementWhenWhenCurrentTimeIsWithinTheGivenRange() throws Exception {
        final TimeRangeAchievementBean timeRangeAchievementBean = givenTimeAchievementBean("08:00", "09:00");
        final String currentTime = "08:31";
        given(mockDateProvider.currentTime()).willReturn(new LocalTime(8, 31).toDateTimeToday().toDate());
        given(mockDateProvider.currentTimeString()).willReturn(currentTime);
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
        given(mockDateProvider.currentTime()).willReturn(new LocalTime(11, 0).toDateTimeToday().toDate());
        given(mockDateProvider.currentTimeString()).willReturn(currentTime);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", currentTime);
        given(mockUnlockedEventFactory.createEvent(timeRangeAchievementBean, currentTime)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    private TimeRangeAchievementBean givenTimeAchievementBean(final String start, final String end) {
        final TimeRangeAchievementBean timeAchievementBean = new TimeRangeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(givenTimeRangeTrigger(start, end));
        return timeAchievementBean;
    }

    private List<TimeTriggerPair> givenTimeRangeTrigger(final String start, final String end) {
        final DateProvider dateProvider = new DateProvider();
        List<TimeTriggerPair> dateTriggers = new ArrayList<>();
        dateTriggers.add(new TimeTriggerPair(dateProvider.parseTime(start), dateProvider.parseTime(end)));
        return dateTriggers;
    }

}
