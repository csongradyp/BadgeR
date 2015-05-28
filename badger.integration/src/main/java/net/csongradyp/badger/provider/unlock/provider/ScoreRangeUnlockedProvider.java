package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class ScoreRangeUnlockedProvider extends UnlockedProvider<ScoreRangeAchievementBean> {

    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final ScoreRangeAchievementBean timeAchievement, final Long score) {
        final List<ScoreTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (ScoreTriggerPair trigger : timeTriggers) {
            if(trigger.fire(score) && !isUnlocked(timeAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(timeAchievement, String.valueOf(score));
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
