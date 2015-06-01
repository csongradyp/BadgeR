package net.csongradyp.badger.provider.unlock;

import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.persistence.EventDao;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class AchievementUnlockProviderFacade implements IAchievementUnlockFinderFacade {

    @Inject
    private EventDao eventDao;
    @Resource(name = "unlockedProviders")
    private Map<AchievementType, IUnlockedProvider<IAchievement>> unlockedProviders;
    private AchievementDefinition achievementDefinition;

    @Override
    public Collection<IAchievementUnlockedEvent> findAll() {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        achievementDefinition.getAll().stream().forEach(achievementBean -> {
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        });
        return unlockables;
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event) {
        final Long currentValue = eventDao.scoreOf(event);
        return findUnlockables(event, currentValue);
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event, final Long newScore) {
        return findUnlockables(event, newScore, Collections.emptySet());
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event, final Collection<String> owners) {
        final Long currentValue = eventDao.scoreOf(event);
        return findUnlockables(event, currentValue, owners);
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event, final Long score, final Collection<String> owners) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementDefinition.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(achievementBean, score);
            if (achievement.isPresent()) {
                final IAchievementUnlockedEvent toUnlock = achievement.get();
                toUnlock.addOwners(owners);
                unlockables.add(toUnlock);
            }
        }
        return unlockables;
    }

    private Optional<IAchievementUnlockedEvent> getUnlockable(final IAchievement achievementBean, final Long currentValue) {
        final IUnlockedProvider<IAchievement> unlockedProvider = unlockedProviders.get(achievementBean.getType());
        return unlockedProvider.getUnlockable(achievementBean, currentValue);
    }

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final IAchievement achievementBean) {
        final Long bestScore = getBestScoreOf(achievementBean.getSubscriptions());
        return getUnlockable(achievementBean, bestScore);
    }

    private Long getBestScoreOf(final List<String> events) {
        Long bestScore = Long.MIN_VALUE;
        for (String event : events) {
            final Long eventScore = eventDao.scoreOf(event);
            if(eventScore > bestScore) {
                bestScore = eventScore;
            }
        }
        return bestScore;
    }

    public void setAchievementDefinition(final AchievementDefinition achievementDefinition) {
        this.achievementDefinition = achievementDefinition;
    }

    void setEventDao(final EventDao eventDao) {
        this.eventDao = eventDao;
    }

    void setUnlockedProviders(final Map<AchievementType, IUnlockedProvider<IAchievement>> unlockedProviders) {
        this.unlockedProviders = unlockedProviders;
    }
}
