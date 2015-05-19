package net.csongradyp.badger.provider.unlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.persistence.EventDao;

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
        for (IAchievement achievementBean : achievementDefinition.getAll()) {
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event) {
        final Long currentValue = eventDao.scoreOf(event);
        return findUnlockables(event, currentValue);
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event, final Long currentValue) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementDefinition.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(achievementBean, currentValue);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    @Override
    public Collection<IAchievementUnlockedEvent> findUnlockables(final String event, final Collection<String> owners) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementDefinition.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Long currentValue = eventDao.scoreOf(event);
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(achievementBean, currentValue);
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
        final IUnlockedProvider<IAchievement> unlockedProvider = unlockedProviders.get(achievementBean.getType());
        final List<String> events = achievementBean.getEvent();
        Long bestScore = Long.MIN_VALUE;
        for (String event : events) {
            final Long eventScore = eventDao.scoreOf(event);
            if(eventScore > bestScore) {
                bestScore = eventScore;
            }
        }

        return unlockedProvider.getUnlockable(achievementBean, bestScore);
    }

    public void setAchievementDefinition(AchievementDefinition achievementDefinition) {
        this.achievementDefinition = achievementDefinition;
    }

}
