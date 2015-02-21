package com.noe.badger.event;


import com.noe.badger.AchievementController;
import com.noe.badger.event.domain.Achievement;
import com.noe.badger.event.domain.AchievementEventType;
import com.noe.badger.event.domain.CheckEvent;
import com.noe.badger.event.domain.IncrementEvent;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(EventBus.class);
    private static final EventBus INSTANCE = new EventBus();

    private AchievementController controller;

    private final MBassador<CheckEvent> checkers;
    private final MBassador<Achievement> subscribers;

    private EventBus() {
        checkers = new MBassador<>(BusConfiguration.SyncAsync());
        subscribers = new MBassador<>(BusConfiguration.SyncAsync());

        registerShutdownHook(checkers, subscribers);
    }

    private void registerShutdownHook(final MBassador<?>... mBassadors) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (MBassador<?> eventBus : mBassadors) {
                eventBus.shutdown();
            }
        }));
    }

    public static void subscribe(final AchievementHandler handler) {
        INSTANCE.subscribers.subscribe(handler);
    }

    public static void unSubscribe(final AchievementHandler handler) {
        INSTANCE.subscribers.unsubscribe(handler);
    }

    public static void publishUnlocked(final Achievement achievement) {
        achievement.setEventType(AchievementEventType.UNLOCK);
        LOG.info(achievement.getTitle() + " unlocked");
        INSTANCE.subscribers.publish(achievement);
    }

    public static void subscribe(final AchievementCheckHandler handler) {
        INSTANCE.checkers.subscribe(handler);
    }

    public static void unSubscribe(final AchievementCheckHandler handler) {
        INSTANCE.checkers.unsubscribe(handler);
    }

    public static void publishCheck(final CheckEvent achievement) {
        INSTANCE.checkers.publish(achievement);
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
