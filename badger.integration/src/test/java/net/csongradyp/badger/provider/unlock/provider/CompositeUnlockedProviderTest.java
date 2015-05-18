package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.CounterAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.relation.ChildAchievement;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.domain.achievement.relation.RelationOperator;
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

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CompositeUnlockedProviderTest {

    private static final String ACHIEVEMENT_ID = "id";
    @Mock
    private IDateProvider mockDateProvider;
    @Mock
    private UnlockedEventFactory mockUnlockedEventFactory;
    @Mock
    protected AchievementDao mockAchievementDao;
    @Mock
    private IAchievementUnlockFinderFacade mockUnlockFinder;

    private CompositeUnlockedProvider underTest;

    @Before
    public void setUp() {
        underTest = new CompositeUnlockedProvider();
        underTest.setUnlockedEventFactory(mockUnlockedEventFactory);
        underTest.setAchievementDao(mockAchievementDao);
        underTest.setUnlockFinder(mockUnlockFinder);
    }

    @Test
    public void testGetUnlockableReturnsUnlockEventWhenAllChildrenAreTriggered() throws Exception {
        final CompositeAchievementBean achievementBean = givenCompositeAchievementBean();
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "");
        given(mockUnlockFinder.getUnlockable(any(IAchievement.class))).willReturn(Optional.of(unlockedEvent));
        given(mockUnlockedEventFactory.createEvent(achievementBean)).willReturn(unlockedEvent);

        final Optional<IAchievementUnlockedEvent> result = underTest.getUnlockable(achievementBean, 0L);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(unlockedEvent));
    }

    private CompositeAchievementBean givenCompositeAchievementBean() {
        final ChildAchievement score = new ChildAchievement(givenScoreAchievementBean());
        final ChildAchievement time = new ChildAchievement(givenTimeRangeAchievementBean());
        final Relation relation = new Relation();
        relation.setOperator(RelationOperator.AND);
        relation.addChild(score);
        relation.addChild(time);
        return new CompositeAchievementBean(ACHIEVEMENT_ID, relation);
    }

    private CounterAchievementBean givenScoreAchievementBean() {
        final CounterAchievementBean timeAchievementBean = new CounterAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        return timeAchievementBean;
    }

    private TimeAchievementBean givenTimeRangeAchievementBean() {
        final TimeAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ACHIEVEMENT_ID);
        return timeAchievementBean;
    }
}
