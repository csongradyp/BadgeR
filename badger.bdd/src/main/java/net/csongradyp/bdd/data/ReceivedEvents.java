package net.csongradyp.bdd.data;

import java.util.ArrayList;
import java.util.List;

public class ReceivedEvents<EVENT_TYPE> {

    private List<EVENT_TYPE> receivedEvents;

    public ReceivedEvents() {
        this.receivedEvents = new ArrayList<>();
    }

    public List<EVENT_TYPE> getAll() {
        return receivedEvents;
    }

    public EVENT_TYPE get(final Integer index) {
        return receivedEvents.get(index);
    }

    public Boolean isEmpty() {
        return receivedEvents.isEmpty();
    }

    public void add(EVENT_TYPE receivedEvent) {
        receivedEvents.add(0, receivedEvent);
    }

    public void clear() {
        receivedEvents = new ArrayList<>();
    }
}
