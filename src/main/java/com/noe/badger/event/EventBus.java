package com.noe.badger.event;


import com.noe.badger.AchievementController;
import com.noe.badger.event.handler.AchievementUnlockedHandlerWrapper;
import com.noe.badger.event.handler.IAchievementUnlockedHandler;
import com.noe.badger.event.handler.IScoreUpdateHandler;
import com.noe.badger.event.handler.ScoreUpdateHandlerWrapper;
import com.noe.badger.event.message.Achievement;
import com.noe.badger.event.message.Score;
import java.util.ArrayList;
import java.util.Collection;
import net.engio.mbassy.bus.MBassador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(EventBus.class);
    private static final EventBus INSTANCE = new EventBus();

    private AchievementController controller;

    private final MBassador<Score> updateSubscribers;
    private final MBassador<Achievement> unlockedSubscribers;

    private EventBus() {
        updateSubscribers = new MBassador<>();
        unlockedSubscribers = new MBassador<>();

        registerShutdownHook(updateSubscribers, unlockedSubscribers);
    }

    private void registerShutdownHook(final MBassador<?>... mBassadors) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (MBassador<?> eventBus : mBassadors) {
                eventBus.shutdown();
            }
        }));
    }

    public static void subscribeOnUnlock(final AchievementUnlockedHandlerWrapper handler) {
        INSTANCE.unlockedSubscribers.subscribe(handler);
    }

    public static void unSubscribeOnUnlock(final IAchievementUnlockedHandler handler) {
        INSTANCE.unlockedSubscribers.unsubscribe(handler);
    }

    public static void publishUnlocked(final Achievement achievement) {
        LOG.info("{} unlocked", achievement.getTitle());
        INSTANCE.unlockedSubscribers.publish(achievement);
    }

    public static void subscribeOnScoreChanged(final ScoreUpdateHandlerWrapper handler) {
        INSTANCE.updateSubscribers.subscribe(handler);
    }

    public static void unSubscribeOnScoreChanged(final IScoreUpdateHandler handler) {
        INSTANCE.updateSubscribers.unsubscribe(handler);
    }

    public static void publishScoreChanged(final Score score) {
        LOG.info("Achievement score {} updated with value {}", score.getEvent(), score.getValue());
        INSTANCE.updateSubscribers.publish(score);
    }

    public static void triggerEvent(final String id, final Long score) {
        INSTANCE.controller.triggerEvent(id, score);
    }

    public static void triggerEvent(final String id, final ArrayList<String> owners) {
        INSTANCE.controller.triggerEvent(id, owners);
    }

    public static void setController(final AchievementController controller) {
        INSTANCE.controller = controller;
    }

    public static void unlock(final String achievementId, final String triggerValue) {
        INSTANCE.controller.unlock(achievementId, triggerValue);
    }

    public static void unlock(final String achievementId, final String triggerValue, final Collection<String> owners) {
        INSTANCE.controller.unlock(achievementId, triggerValue, owners);
    }

    public static void checkAll() {
        INSTANCE.controller.checkAll();
    }
}
