package net.csongradyp.badger.provider.unlock.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.provider.date.IDateProvider;
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
    public void testUnlockableReturnsUnlockableAchievementWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecisionAndAchievementIsNotUnlocked() {
        final String time = "23:14";
        final TimeAchievementBean timeAchievementBean = givenTimeAchievementBean(time);
        given(mockDateProvider.currentTime()).willReturn(new DateTime(2015, 1, 1, 23, 14).toDate());
        given(mockDateProvider.currentTimeString()).willReturn(time);
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
        final TimeAchievementBean timeAchievementBean = givenTimeAchievementBean(time);
        given(mockDateProvider.currentTime()).willReturn(new DateTime(2015, 1, 1, 23, 14).toDate());
        given(mockDateProvider.currentTimeString()).willReturn(time);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(true);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeAchievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenNoTriggerIsMatchingTheCurrentTime() {
        final TimeAchievementBean timeAchievementBean = givenTimeAchievementBean("23:14");
        given(mockDateProvider.currentTime()).willReturn(new DateTime(2015, 1, 1, 23, 15).toDate());
        given(mockDateProvider.currentTimeString()).willReturn("23:15");

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeAchievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    private TimeAchievementBean givenTimeAchievementBean(final String trigger) {
        final TimeAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(givenTimeTrigger(trigger));
        return timeAchievementBean;
    }

    private List<TimeTrigger> givenTimeTrigger(final String time) {
        final DateProvider dateProvider = new DateProvider();
        List<TimeTrigger> dateTriggers = new ArrayList<>();
        dateTriggers.add(new TimeTrigger(dateProvider.parseTime(time)));
        return dateTriggers;
    }

}
