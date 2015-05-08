package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.bundle.domain.TestAchievementBean;
import com.noe.badger.bundle.domain.achievement.DateAchievementBean;
import com.noe.badger.bundle.domain.achievement.TimeAchievementBean;
import com.noe.badger.bundle.parser.IAchievementDefinitionFileParser;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.handler.AchievementUnlockedHandlerWrapper;
import com.noe.badger.event.message.Achievement;
import com.noe.badger.persistence.AchievementDao;
import com.noe.badger.persistence.CounterDao;
import com.noe.badger.util.DateFormatUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AchievementControllerTest {

    private static final String ACHIEVEMENT_ID = "test";

    @Mock
    private IAchievementDefinitionFileParser mockAchievementParser;
    @Mock
    private AchievementBundle mockAchievementBundle;
    @Mock
    private AchievementDao mockAchievementDao;
    @Mock
    private CounterDao mockCounterDao;

    private AchievementController underTest;

    @Before
    public void setUp() {
        underTest = new AchievementController();
        underTest.setDefinition(mockAchievementBundle);
        underTest.setAchievementDao(mockAchievementDao);
        underTest.setCounterDao(mockCounterDao);
    }

    @Test
    public void testGetAllCallUnderlyingBundle() {
        final TestAchievementBean testAchievementBean = new TestAchievementBean(ACHIEVEMENT_ID);
        final ArrayList<IAchievement> parsedAchievements = new ArrayList<>();
        parsedAchievements.add(testAchievementBean);
        when(mockAchievementBundle.getAll()).thenReturn(parsedAchievements);

        final Collection<IAchievement> achievements = underTest.getAll();

        verify(mockAchievementBundle).getAll();
        assertThat(achievements.size(), is(equalTo(1)));
        assertThat(achievements.iterator().next(), is(testAchievementBean));
    }

    @Test
    public void testUnlockPublishUnlockedEventWithProperData() {
        final AchievementUnlockedHandlerWrapper handler = new AchievementUnlockedHandlerWrapper(achievement -> {
            assertThat(achievement.getId(), is(equalTo(ACHIEVEMENT_ID)));
            assertThat(achievement.getLevel(), is(equalTo(1)));
        });
        when(mockAchievementBundle.get(ACHIEVEMENT_ID)).thenReturn(Optional.of(new TestAchievementBean(ACHIEVEMENT_ID)));
        when(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID, 1)).thenReturn(false);
        EventBus.subscribeOnUnlock(handler);

        underTest.unlock(ACHIEVEMENT_ID, "1");

        verify(mockAchievementBundle).get(ACHIEVEMENT_ID);
        verify(mockAchievementDao).isUnlocked(ACHIEVEMENT_ID, 1);
        EventBus.unSubscribeOnUnlock(handler);
    }

    @Test
    public void testIsUnlockedCallsUnderlyingDao() {
        underTest.isUnlocked(ACHIEVEMENT_ID);
        verify(mockAchievementDao).isUnlocked(ACHIEVEMENT_ID);
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenGivenDateAchievementTriggerIsEqualToTheCurrentDate() throws Exception {
        final IAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ACHIEVEMENT_ID);
        dateAchievementBean.setTrigger(new String[]{DateFormatUtil.formatDate(new Date())});

        final Optional<Achievement> result = underTest.unlockable(0L, dateAchievementBean);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(equalTo(ACHIEVEMENT_ID)));
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecision() throws Exception {
        final IAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(new String[]{DateFormatUtil.formatTime(new Date())});

        final Optional<Achievement> result = underTest.unlockable(0L, timeAchievementBean);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(equalTo(ACHIEVEMENT_ID)));
    }
}
