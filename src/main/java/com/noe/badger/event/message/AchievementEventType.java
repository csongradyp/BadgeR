package com.noe.badger.event.message;

/**
 * Type of the unlock event - Newly unlocked or level up.
 */
public enum AchievementEventType {

    /**
     * First level of the achievement was unlocked.
     */
    UNLOCK,
    /**
     * Second or higher level was unlocked.
     */
    LEVEL_UP
}
