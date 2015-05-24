package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class ScoreUnlockedProvider extends UnlockedProvider<ScoreAchievementBean> {

    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final ScoreAchievementBean achievementBean, final Long currentValue) {
        final List<ScoreTrigger> triggers = achievementBean.getTrigger();
        for (int i = 0; i < triggers.size(); i++) {
            final Integer level = i + 1;
            if (triggers.get(i).fire(currentValue) && isLevelValid(achievementBean, level) && !isLevelUnlocked(achievementBean.getId(), level)) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(achievementBean, level, currentValue);
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    private boolean isLevelValid(final IAchievementBean counterAchievement, final Integer triggerIndex) {
        return counterAchievement.getMaxLevel() >= triggerIndex;
    }

    private Boolean isLevelUnlocked(final String id, final Integer level) {
        return achievementDao.isUnlocked(id, level);
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
