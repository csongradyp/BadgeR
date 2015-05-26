package net.csongradyp.badger.exception;

public class MalformedAchievementDefinition extends RuntimeException {

    public MalformedAchievementDefinition(final String message) {
        super(message);
    }

    public MalformedAchievementDefinition(final String message, Throwable cause) {
        super(message, cause);
    }
}
