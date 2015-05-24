package net.csongradyp.badger.provider.unlock.provider;

import java.util.Date;
import java.util.Optional;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.provider.date.IDateProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CompositeUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private IDateProvider mockDateProvider;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;
    @Mock
    private Relation mockRelation;

    private CompositeUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new CompositeUnlockedProvider();
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
        underTest.setDateProvider(mockDateProvider);
    }

    @Test
    public void testGetUnlockableReturnsUnlockEventWhenTriggerRelationEvaluationReturnsTrueAndAchievementIsNotUnlocked() throws Exception {
        final CompositeAchievementBean achievementBean = givenCompositeAchievementBean();
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "");
        given(mockRelation.evaluate(any(Long.class), any(Date.class), any(Date.class))).willReturn(true);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);
        given(mockUnlockedEventFactory.createEvent(achievementBean, "0")).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(achievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTriggerRelationEvaluationReturnsFalseAndAchievementIsNotUnlocked() throws Exception {
        final CompositeAchievementBean achievementBean = givenCompositeAchievementBean();
        given(mockRelation.evaluate(any(Long.class), any(Date.class), any(Date.class))).willReturn(false);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(false);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(achievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTriggerRelationEvaluationReturnsTrueAndAchievementIsUnlocked() throws Exception {
        final CompositeAchievementBean achievementBean = givenCompositeAchievementBean();
        given(mockRelation.evaluate(any(Long.class), any(Date.class), any(Date.class))).willReturn(true);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(true);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(achievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testGetUnlockableReturnsEmptyWhenTriggerRelationEvaluationReturnsFalseAndAchievementIsUnlocked() throws Exception {
        final CompositeAchievementBean achievementBean = givenCompositeAchievementBean();
        given(mockRelation.evaluate(any(Long.class), any(Date.class), any(Date.class))).willReturn(false);
        given(mockAchievementDao.isUnlocked(ACHIEVEMENT_ID)).willReturn(true);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(achievementBean, 0L);

        assertThat(result.isPresent(), is(false));
    }

    private CompositeAchievementBean givenCompositeAchievementBean() {
        final CompositeAchievementBean achievementBean = new CompositeAchievementBean();
        achievementBean.setId(ACHIEVEMENT_ID);
        achievementBean.setRelation(mockRelation);
        return achievementBean;
    }
}
