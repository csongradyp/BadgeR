package net.csongradyp.badger.provider.unlock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.persistence.EventDao;
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
public class AchievementUnlockProviderFacadeTest {

    private static final String ACHIEVEMENT_ID = "test";
    private static final String EVENT_ID = "event";

    @Mock
    private EventDao mockEventDao;
    @Mock
    private AchievementDefinition mockAchievementDefinition;
    @Mock
    private Map<AchievementType, IUnlockedProvider<IAchievement>> mockUnlockedProviders;
    @Mock
    private IUnlockedProvider mockUnlockedProvider;

    private AchievementUnlockProviderFacade underTest;

    @Before
    public void setUp() {
        underTest = new AchievementUnlockProviderFacade();
        underTest.setEventDao(mockEventDao);
        underTest.setAchievementDefinition(mockAchievementDefinition);
        underTest.setUnlockedProviders(mockUnlockedProviders);
    }

    @Test
    public void testFindAllReturnsAllUnlockableAchievementsAsUnlockEvents() throws Exception {
        final Long score = 1L;
        final ScoreAchievementBean achievementBean = new ScoreAchievementBean();
        achievementBean.setEvent(new String[] {EVENT_ID});
        given(mockAchievementDefinition.getAll()).willReturn(Collections.singletonList(achievementBean));
        given(mockEventDao.scoreOf(EVENT_ID)).willReturn(score);
        given(mockUnlockedProviders.get(AchievementType.SCORE)).willReturn(mockUnlockedProvider);
        final IAchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "");
        given(mockUnlockedProvider.getUnlockable(achievementBean, score)).willReturn(Optional.of(unlockedEvent));

        final Collection<IAchievementUnlockedEvent> result = underTest.findAll();

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.iterator().next(), is(unlockedEvent));
    }

    @Test
    public void testFindUnlockablesReturnsAllUnlockableAchievementsAsUnlockEventsSubscribedForEvent() throws Exception {
        final Long score = 1L;
        given(mockEventDao.scoreOf(EVENT_ID)).willReturn(score);
        final ScoreAchievementBean achievementBean = new ScoreAchievementBean();
        achievementBean.setEvent(new String[]{EVENT_ID});
        given(mockAchievementDefinition.getAchievementsSubscribedFor(EVENT_ID)).willReturn(Collections.singletonList(achievementBean));
        given(mockUnlockedProviders.get(AchievementType.SCORE)).willReturn(mockUnlockedProvider);
        final IAchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "");
        given(mockUnlockedProvider.getUnlockable(achievementBean, score)).willReturn(Optional.of(unlockedEvent));

        final Collection<IAchievementUnlockedEvent> result = underTest.findUnlockables(EVENT_ID);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.iterator().next(), is(unlockedEvent));
    }

    @Test
    public void testFindUnlockablesWithGivenOwnersReturnsAllUnlockableAchievementsAsUnlockEvents() throws Exception {
        final Long score = 1L;
        final Set<String> owners = new HashSet<>(Arrays.asList("owner"));
        given(mockEventDao.scoreOf(EVENT_ID)).willReturn(score);
        final ScoreAchievementBean achievementBean = new ScoreAchievementBean();
        achievementBean.setEvent(new String[]{EVENT_ID});
        given(mockAchievementDefinition.getAchievementsSubscribedFor(EVENT_ID)).willReturn(Collections.singletonList(achievementBean));
        given(mockUnlockedProviders.get(AchievementType.SCORE)).willReturn(mockUnlockedProvider);
        final IAchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(ACHIEVEMENT_ID, "", "", "");
        given(mockUnlockedProvider.getUnlockable(achievementBean, score)).willReturn(Optional.of(unlockedEvent));

        final Collection<IAchievementUnlockedEvent> result = underTest.findUnlockables(EVENT_ID, owners);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.iterator().next(), is(unlockedEvent));
        assertThat(result.iterator().next().getOwners(), is(equalTo(owners)));
    }
}
