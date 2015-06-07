package net.csongradyp.badger.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.inject.Named;
import net.csongradyp.badger.event.exception.SubscriptionException;
import net.csongradyp.badger.event.handler.IAchievementUnlockedHandler;
import net.csongradyp.badger.event.handler.IScoreUpdateHandler;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.engio.mbassy.bus.MBassador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(EventBus.class);

    private final MBassador<ScoreUpdatedEvent> scoreUpdateBus;
    private final MBassador<IAchievementUnlockedEvent> unlockedBus;
    private final Collection<AchievementUnlockedHandlerWrapper> unlockedSubscribers = new ArrayList<>();
    private final Collection<ScoreUpdateHandlerWrapper> scoreUpdateSubscribers = new ArrayList<>();

    public EventBus() {
        scoreUpdateBus = new MBassador<>();
        unlockedBus = new MBassador<>();
        registerShutdownHook(scoreUpdateBus, unlockedBus);
    }

    private void registerShutdownHook(final MBassador<?>... mBassadors) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (MBassador eventBus : mBassadors) {
                eventBus.shutdown();
            }
        }));
    }

    public void subscribeOnUnlock(final AchievementUnlockedHandlerWrapper handler) {
        unlockedSubscribers.add(handler);
        unlockedBus.subscribe(handler);
    }

    public void unSubscribeOnUnlock(final IAchievementUnlockedHandler handler) {
        final Optional<AchievementUnlockedHandlerWrapper> registeredHandler = unlockedSubscribers.stream()
                .filter(wrapper -> wrapper.getWrapped().equals(handler))
                .findAny();
        if (registeredHandler.isPresent()) {
            final AchievementUnlockedHandlerWrapper listener = registeredHandler.get();
            unSubscribe(listener);
        }
    }

    private void unSubscribe(final AchievementUnlockedHandlerWrapper listener) {
        final boolean unsubscribe = unlockedBus.unsubscribe(listener);
        if (!unsubscribe) {
            throw new SubscriptionException("Unsubscribe failed for achievement unlocked handler" + listener.getWrapped());
        }
        unlockedSubscribers.remove(listener);
    }

    public void publishUnlocked(final IAchievementUnlockedEvent achievement) {
        unlockedBus.publish(achievement);
        LOG.info("Achievement unlocked event published. Achievement - title: {}, level: {}", achievement.getTitle(), achievement.getLevel());
    }

    public void subscribeOnScoreChanged(final ScoreUpdateHandlerWrapper handler) {
        scoreUpdateSubscribers.add(handler);
        scoreUpdateBus.subscribe(handler);
    }

    public void unSubscribeAllOnScoreChanged() {
        scoreUpdateSubscribers.stream().forEach( scoreUpdateBus::unsubscribe );
        scoreUpdateSubscribers.clear();
    }

    public void unSubscribeOnScoreChanged(final IScoreUpdateHandler handler) {
        final Optional<ScoreUpdateHandlerWrapper> registeredHandler = scoreUpdateSubscribers.stream()
                .filter(wrapper -> wrapper.getWrapped().equals(handler))
                .findAny();
        if (registeredHandler.isPresent()) {
            final ScoreUpdateHandlerWrapper listener = registeredHandler.get();
            unSubscribe(listener);
        }
    }

    private void unSubscribe(final ScoreUpdateHandlerWrapper listener) {
        final boolean unsubscribe = scoreUpdateBus.unsubscribe(listener);
        if (!unsubscribe) {
            throw new SubscriptionException("Unsubscribe failed for score changed handler" + listener.getWrapped());
        }
        scoreUpdateSubscribers.remove(listener);
    }

    public void publishScoreChanged(final ScoreUpdatedEvent scoreUpdatedEvent) {
        scoreUpdateBus.publish(scoreUpdatedEvent);
        LOG.info("Achievement score {} updated with value {}", scoreUpdatedEvent.getEvent(), scoreUpdatedEvent.getValue());
    }

    public Collection<AchievementUnlockedHandlerWrapper> getUnlockedSubscribers() {
        return unlockedSubscribers;
    }

    public Collection<ScoreUpdateHandlerWrapper> getScoreUpdateSubscribers() {
        return scoreUpdateSubscribers;
    }
}
