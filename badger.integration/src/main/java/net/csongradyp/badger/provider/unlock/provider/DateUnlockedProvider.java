package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
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
public class DateUnlockedProvider extends UnlockedProvider<DateAchievementBean> {

    @Inject
    private IDateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final DateAchievementBean dateAchievement, final Long score) {
        final List<DateTrigger> dateTriggers = dateAchievement.getTrigger();
        final String nowString = dateProvider.currentDateString();
        final Date now = dateProvider.currentDate();
        for (DateTrigger dateTrigger : dateTriggers) {
            if (dateTrigger.fire(now) && !isUnlocked(dateAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(dateAchievement, nowString);
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    public void setDateProvider(final IDateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
