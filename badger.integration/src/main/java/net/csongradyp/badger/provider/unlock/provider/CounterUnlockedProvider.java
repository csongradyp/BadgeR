package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.CounterAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.NumberTrigger;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class CounterUnlockedProvider extends UnlockedProvider<CounterAchievementBean> {

    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final CounterAchievementBean achievementBean, final Long currentValue) {
        final List<NumberTrigger> triggers = achievementBean.getTrigger();
        for (int i = 0; i < triggers.size(); i++) {
            final NumberTrigger trigger = triggers.get(i);
            final Integer level = i + 1;
            if (isTriggered(currentValue, trigger) && isLevelValid(achievementBean, level) && !isLevelUnlocked(achievementBean.getId(), level)) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(achievementBean, level, currentValue);
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    private Boolean isTriggered(final Long currentValue, final NumberTrigger trigger) {
        Boolean triggered = false;
        switch (trigger.getOperation()) {
            case GREATER_THAN:
                triggered = currentValue >= trigger.getTrigger();
                break;
            case LESS_THAN:
                triggered = currentValue <= trigger.getTrigger();
                break;
            case EQUALS:
                triggered = currentValue.equals(trigger.getTrigger());
                break;
        }
        return triggered;
    }


    private boolean isLevelValid(final IAchievementBean<NumberTrigger> counterAchievement, final Integer triggerIndex) {
        return counterAchievement.getMaxLevel() >= triggerIndex;
    }

    private Boolean isLevelUnlocked(final String id, final Integer level) {
        return achievementDao.isUnlocked(id, level);
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
