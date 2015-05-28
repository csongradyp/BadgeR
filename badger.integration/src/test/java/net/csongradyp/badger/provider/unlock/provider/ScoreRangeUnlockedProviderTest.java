package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ScoreRangeUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;

    private ScoreRangeUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new ScoreRangeUnlockedProvider();
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenGivenScoreIsNotWithinTheGivenRange() throws Exception {
        final ScoreRangeAchievementBean achievementBean = givenScoreAchievementBean(1L, 10L);
        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(achievementBean, 0L);
        assertThat(result.isPresent(), is(false));

        final Optional<IAchievementUnlockedEvent> result2 = underTest.getUnlockable(achievementBean, 11L);
        assertThat(result2.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsnlockableAchievementWhenWhenGivenScoreIsWithinTheGivenRange() throws Exception {
        final ScoreRangeAchievementBean timeRangeAchievementBean = givenScoreAchievementBean(1L, 10L);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "5");
        given(mockUnlockedEventFactory.createEvent(timeRangeAchievementBean, "5")).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 5L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsnlockableAchievementWhenWhenGivenScoreIsWithinReversedGivenRange() throws Exception {
        final ScoreRangeAchievementBean timeRangeAchievementBean = givenScoreAchievementBean(10L, 1L);
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "11");
        given(mockUnlockedEventFactory.createEvent(timeRangeAchievementBean, "11")).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(timeRangeAchievementBean, 11L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    private ScoreRangeAchievementBean givenScoreAchievementBean(final Long start, final Long end) {
        final ScoreRangeAchievementBean timeAchievementBean = new ScoreRangeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        timeAchievementBean.setTrigger(givenScoreRangeTrigger(start, end));
        return timeAchievementBean;
    }

    private List<ScoreTriggerPair> givenScoreRangeTrigger(final Long start, final Long end) {
        List<ScoreTriggerPair> dateTriggers = new ArrayList<>();
        dateTriggers.add(new ScoreTriggerPair(start, end));
        return dateTriggers;
    }

}
