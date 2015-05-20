package net.csongradyp.badger.persistence.exception;

public class UnlockedAchievementNotFoundException extends RuntimeException {

    public UnlockedAchievementNotFoundException(final String achievementId) {
        super("Unlocked achievement not with id: " + achievementId);
    }
}
