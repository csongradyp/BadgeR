package com.noe.badger.event;

import com.noe.badger.event.domain.Achievement;
import com.noe.badger.event.domain.AchievementEventType;

import javax.inject.Named;
import java.util.HashSet;
import java.util.Set;

@Named
public class EventBus {

    private final Set<AchievementHandler> subscribers;

    public EventBus() {
        subscribers = new HashSet<>();
    }

    public void subscribe(final AchievementHandler handler) {
        subscribers.add(handler);
    }

    public void unSubscribe(final AchievementHandler handler) {
        subscribers.remove(handler);
    }

    public void publishUnlocked(final Achievement achievement) {
        achievement.setEventType( AchievementEventType.UNLOCK);
        for (AchievementHandler subscriber : subscribers) {
            subscriber.onUnlocked(achievement);
        }
    }
}
