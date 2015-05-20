package net.csongradyp.badger.persistence.exception;

public class MissingEventCounterException extends RuntimeException {

    public MissingEventCounterException(String eventId) {
        super("No counter registered for event: " + eventId);
    }
}
