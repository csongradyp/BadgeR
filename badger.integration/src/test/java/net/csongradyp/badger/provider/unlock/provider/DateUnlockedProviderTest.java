package net.csongradyp.badger.provider.unlock.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DateUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    @Mock
    private IDateProvider mockDateProvider;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;

    private DateUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new DateUnlockedProvider();
        underTest.setDateProvider(mockDateProvider);
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenGivenDateAchievementTriggerIsEqualToTheCurrentDateAndAchievementIsNotUnlocked() {
        final String date = "02-14";
        final DateAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ACHIEVEMENT_ID);
        dateAchievementBean.setTrigger(givenDateTrigger(date));
        given(mockDateProvider.currentDate()).willReturn(new DateTime(2015, 2, 14, 0, 0).toDate());
        given(mockDateProvider.currentDateString()).willReturn(date);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", date);
        given(mockUnlockedEventFactory.createEvent(dateAchievementBean, date)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(dateAchievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(equalTo(unlockedEvent)));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenGivenDateAchievementTriggerIsEqualToTheCurrentDateButTheAchievementIsAleadyUnlocked() {
        final String date = "02-14";
        final DateAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ACHIEVEMENT_ID);
        dateAchievementBean.setTrigger(givenDateTrigger(date));
        given(mockDateProvider.currentDate()).willReturn(new DateTime(2015, 2, 14, 0, 0).toDate());
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(true);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(dateAchievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testUnlockableReturnsEmptyWhenGivenDateAchievementTriggerIsNotEqualToTheCurrentDate() {
        final String trigger = "01-01";
        final DateAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ACHIEVEMENT_ID);
        dateAchievementBean.setTrigger(givenDateTrigger(trigger));
        given(mockDateProvider.currentDate()).willReturn(new DateTime(2015, 2, 14, 0, 0).toDate());
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(dateAchievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    private List<DateTrigger> givenDateTrigger(final String date) {
        final DateProvider dateProvider = new DateProvider();
        List<DateTrigger> dateTriggers = new ArrayList<>();
        dateTriggers.add(new DateTrigger(dateProvider.parseDate(date)));
        return dateTriggers;
    }

}
