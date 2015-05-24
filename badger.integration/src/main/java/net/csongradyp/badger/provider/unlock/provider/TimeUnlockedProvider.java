package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.IDateProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Named
public class TimeUnlockedProvider extends UnlockedProvider<TimeAchievementBean> {

    @Inject
    private IDateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    public Optional<IAchievementUnlockedEvent> getUnlockable(final TimeAchievementBean timeAchievement, final Long score) {
        final List<TimeTrigger> timeTriggers = timeAchievement.getTrigger();
        final String nowString = dateProvider.currentTimeString();
        final Date now = dateProvider.currentTime();
        for (TimeTrigger timeTrigger : timeTriggers) {
            if (timeTrigger.fire(now) && !isUnlocked(timeAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(timeAchievement, nowString);
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    public void setDateProvider(IDateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    void setUnlockedEventFactory(UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
