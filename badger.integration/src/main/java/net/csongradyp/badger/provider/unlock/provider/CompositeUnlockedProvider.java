package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.IDateProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Optional;

@Named
public class CompositeUnlockedProvider extends UnlockedProvider<CompositeAchievementBean> {

    @Inject
    private IDateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final CompositeAchievementBean compositeAchievement, final Long score) {
        final Date currentDate = dateProvider.currentDate();
        final Date currentTime = dateProvider.currentTime();
        if (compositeAchievement.evaluate(score, currentDate, currentTime) & !isUnlocked(compositeAchievement.getId())) {
            final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(compositeAchievement, score.toString());
            return Optional.of(achievementUnlockedEvent);
        }
        return Optional.empty();
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }

    public void setDateProvider(final IDateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }
}
