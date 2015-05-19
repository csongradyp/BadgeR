package net.csongradyp.badger.provider.unlock.provider;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.IDateProvider;

@Named
public class DateUnlockedProvider extends UnlockedProvider<DateAchievementBean> {

    @Inject
    private IDateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final DateAchievementBean dateAchievement, final Long score) {
        final List<String> dateTriggers = dateAchievement.getTrigger();
        final String now = dateProvider.currentDate();
        for (String dateTrigger : dateTriggers) {
            if (dateTrigger.equals(now) && !isUnlocked(dateAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(dateAchievement, now);
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
