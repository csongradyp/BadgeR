package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.provider.date.IDateProvider;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class TimeUnlockedProvider extends UnlockedProvider<TimeAchievementBean> {

    @Inject
    private IDateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    public Optional<IAchievementUnlockedEvent> getUnlockable(final TimeAchievementBean timeAchievement, final Long score) {
        final List<String> timeTriggers = timeAchievement.getTrigger();
        final String now = dateProvider.currentTime();
        for (String timeTrigger : timeTriggers) {
            if (timeTrigger.equals(now) && !isUnlocked(timeAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(timeAchievement, now);
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
