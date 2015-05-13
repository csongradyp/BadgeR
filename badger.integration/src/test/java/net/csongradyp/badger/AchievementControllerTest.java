package net.csongradyp.badger;

import net.csongrady.badger.domain.IAchievement;
import net.csongrady.badger.domain.IAchievementBean;
import net.csongrady.badger.event.IAchievementUnlockedEvent;
import net.csongrady.badger.parser.IAchievementDefinitionFileParser;
import net.csongradyp.badger.domain.TestCounterAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.persistence.CounterDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
    private DateProvider dateProvider;

    private AchievementController underTest;

    @Before
    public void setUp() {
        underTest = new AchievementController();
        underTest.setDefinition(mockAchievementBundle);
        underTest.setAchievementDao(mockAchievementDao);
        underTest.setCounterDao(mockCounterDao);
        dateProvider = new DateProvider();
        underTest.setDateProvider(dateProvider);
    }

    @Test
    public void testGetAllCallUnderlyingBundle() {
        final TestCounterAchievementBean testAchievementBean = new TestCounterAchievementBean(ACHIEVEMENT_ID);
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
        when(mockAchievementBundle.get(ACHIEVEMENT_ID)).thenReturn(Optional.of(new TestCounterAchievementBean(ACHIEVEMENT_ID)));
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
        dateAchievementBean.setTrigger(new String[]{dateProvider.now()});

        final Optional<IAchievementUnlockedEvent> result = underTest.unlockable(0L, dateAchievementBean);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(equalTo(ACHIEVEMENT_ID)));
    }

    @Test
    public void testUnlockableReturnsUnlockableAchievementWhenOneOfTheGivenTimeAchievementTriggerIsEqualToTheCurrentTimeInMinutePrecision() throws Exception {
        final IAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(new String[]{dateProvider.currentTime()});

        final Optional<IAchievementUnlockedEvent> result = underTest.unlockable(0L, timeAchievementBean);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(equalTo(ACHIEVEMENT_ID)));
    }
}
