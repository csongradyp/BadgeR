package com.noe.badger.event;


import com.noe.badger.AchievementController;
import com.noe.badger.event.domain.Achievement;
import com.noe.badger.event.domain.AchievementEventType;
import com.noe.badger.event.domain.IncrementEvent;
import com.noe.badger.event.handler.AchievementUnlockedHandlerWrapper;
import com.noe.badger.event.handler.IAchievementUnlockedHandler;
import com.noe.badger.event.handler.IAchievementUpdateHandler;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(EventBus.class);
    private static final EventBus INSTANCE = new EventBus();

    private AchievementController controller;

    private final MBassador<Achievement> updateSubscribers;
    private final MBassador<Achievement> unlockedSubscribers;

    private EventBus() {
        updateSubscribers = new MBassador<>(BusConfiguration.SyncAsync());
        unlockedSubscribers = new MBassador<>(BusConfiguration.SyncAsync());

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
        achievement.setEventType(AchievementEventType.UNLOCK);
        LOG.info(achievement.getTitle() + " unlocked");
        INSTANCE.unlockedSubscribers.publish(achievement);
    }

    public static void subscribeOnUpdate(final AchievementUnlockedHandlerWrapper handler) {
        INSTANCE.updateSubscribers.subscribe(handler);
    }

    public static void unSubscribeOnUpdate(final IAchievementUpdateHandler handler) {
        INSTANCE.updateSubscribers.unsubscribe(handler);
    }

    public static void publishCheck(final Achievement achievement) {
        INSTANCE.updateSubscribers.publish(achievement);
    }

    public static void incrementAndCheck(final IncrementEvent event) {
        getController().incrementAndCheck(event.getId());
    }

    public static void setController(final AchievementController controller) {
        INSTANCE.controller = controller;
    }

    public static AchievementController getController() {
        return INSTANCE.controller;
    }
}