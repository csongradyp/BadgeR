package net.csongradyp.bdd.data;

import net.csongradyp.badger.event.IAchievementUnlockedEvent;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class ReceivedAchievementUnlockedEvents {

    private List<IAchievementUnlockedEvent> receivedEvents;

    public ReceivedAchievementUnlockedEvents() {
        this.receivedEvents = new ArrayList<>();
    }

    public List<IAchievementUnlockedEvent> getAll() {
        return receivedEvents;
    }

    public IAchievementUnlockedEvent get(final Integer index) {
        return receivedEvents.get(index);
    }

    public Boolean isEmpty() {
        return receivedEvents.isEmpty();
    }

    public void add(IAchievementUnlockedEvent receivedEvent) {
        receivedEvents.add(0, receivedEvent);
    }

    public void clear() {
        receivedEvents = new ArrayList<>();
    }
}
