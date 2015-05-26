package net.csongradyp.badger.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.event.exception.SubscriptionException;
import net.csongradyp.badger.event.handler.IAchievementUnlockedHandler;
import net.csongradyp.badger.event.handler.IScoreUpdateHandler;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.engio.mbassy.bus.MBassador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(EventBus.class);
    private static final EventBus INSTANCE = new EventBus();

    private IAchievementController controller;

    private final MBassador<ScoreUpdatedEvent> scoreUpdateBus;
    private final MBassador<IAchievementUnlockedEvent> unlockedBus;
    private static final Collection<AchievementUnlockedHandlerWrapper> unlockedSubscribers = new ArrayList<>();
    private static final Collection<ScoreUpdateHandlerWrapper> scoreUpdateSubscribers= new ArrayList<>();

    private EventBus() {
        scoreUpdateBus = new MBassador<>();
        unlockedBus = new MBassador<>();

        registerShutdownHook(scoreUpdateBus, unlockedBus);
    }

    private void registerShutdownHook(final MBassador<?>... mBassadors) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (MBassador<?> eventBus : mBassadors) {
                eventBus.shutdown();
            }
        }));
    }

    public static void subscribeOnUnlock(final AchievementUnlockedHandlerWrapper handler) {
        unlockedSubscribers.add(handler);
        INSTANCE.unlockedBus.subscribe(handler);
    }

    public static void unSubscribeOnUnlock(final IAchievementUnlockedHandler handler) {
        final Optional<AchievementUnlockedHandlerWrapper> registeredHandler = unlockedSubscribers.stream()
                .filter(wrapper -> wrapper.getWrapped().equals(handler))
                .findAny();
        if (registeredHandler.isPresent()) {
            final AchievementUnlockedHandlerWrapper listener = registeredHandler.get();
            unSubscribe(listener);
        }
    }

    private static void unSubscribe(final AchievementUnlockedHandlerWrapper listener) {
        final boolean unsubscribe = INSTANCE.unlockedBus.unsubscribe(listener);
        if(!unsubscribe) {
            throw new SubscriptionException("Unsubscribe failed for achievement unlocked handler" + listener.getWrapped());
        }
        unlockedSubscribers.remove(listener);
    }

    public static void publishUnlocked(final IAchievementUnlockedEvent achievement) {
        INSTANCE.unlockedBus.publish(achievement);
        LOG.info("Achievement unlocked event published. Achievement - title: {}, level: {}", achievement.getTitle(), achievement.getLevel());
    }

    public static void subscribeOnScoreChanged(final ScoreUpdateHandlerWrapper handler) {
        scoreUpdateSubscribers.add(handler);
        INSTANCE.scoreUpdateBus.subscribe(handler);
    }

    public static void unSubscribeOnScoreChanged(final IScoreUpdateHandler handler) {
        final Optional<ScoreUpdateHandlerWrapper> registeredHandler = scoreUpdateSubscribers.stream()
                .filter(wrapper -> wrapper.getWrapped().equals(handler))
                .findAny();
        if (registeredHandler.isPresent()) {
            final ScoreUpdateHandlerWrapper listener = registeredHandler.get();
            unSubscribe(listener);
        }
    }

    private static void unSubscribe(final ScoreUpdateHandlerWrapper listener) {
        final boolean unsubscribe = INSTANCE.scoreUpdateBus.unsubscribe(listener);
        if(!unsubscribe) {
            throw new SubscriptionException("Unsubscribe failed for score changed handler" + listener.getWrapped());
        }
        scoreUpdateSubscribers.remove(listener);
    }

    public static void publishScoreChanged(final ScoreUpdatedEvent scoreUpdatedEvent) {
        INSTANCE.scoreUpdateBus.publish(scoreUpdatedEvent);
        LOG.info("Achievement score {} updated with value {}", scoreUpdatedEvent.getEvent(), scoreUpdatedEvent.getValue());
    }

    public static void triggerEvent(final String id, final Long score) {
        INSTANCE.controller.triggerEvent(id, score);
    }

    public static void triggerEvent(final String id, final Collection<String> owners) {
        INSTANCE.controller.triggerEvent(id, owners);
    }

    public static void unlock(final String achievementId, final String triggerValue) {
        INSTANCE.controller.unlock(achievementId, triggerValue);
    }

    public static void unlock(final String achievementId, final String triggerValue, final Collection<String> owners) {
        INSTANCE.controller.unlock(achievementId, triggerValue, owners);
    }

    public static void checkAll() {
        INSTANCE.controller.checkAndUnlock();
    }

    public static Collection<AchievementUnlockedHandlerWrapper> getUnlockedSubscribers() {
        return unlockedSubscribers;
    }

    public static Collection<ScoreUpdateHandlerWrapper> getScoreUpdateSubscribers() {
        return scoreUpdateSubscribers;
    }

    public static void setController(final IAchievementController controller) {
        INSTANCE.controller = controller;
    }
}
