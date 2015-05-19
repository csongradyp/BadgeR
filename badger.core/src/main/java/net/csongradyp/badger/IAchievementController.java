package net.csongradyp.badger;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;

public interface IAchievementController {

    void setAchievementDefinition(final AchievementDefinition definition);

    void setInternationalizationBaseName(final String internationalizationBaseName);

    void setResourceBundle(final ResourceBundle resourceBundle);

    void setLocale(final Locale locale);

    Collection<IAchievement> getAll();

    Collection<IAchievement> getByOwner(final String owner);

    Optional<IAchievement> get(final AchievementType type, final String id);

    Map<String, Set<IAchievement>> getAllByEvents();

    void unlockAllUnlockable();

    void triggerEventWithHighScore(final String event, final Long score);

    void triggerEvent(final String event, final Long score);

    void triggerEvent(final String event, final String... owners);

    void triggerEvent(final String event, final Collection<String> owners);

    void triggerEvent(final String event);

    void unlock(final AchievementType type, final String achievementId, String triggeredValue);

    void unlock(final AchievementType type, final String achievementId, final String triggeredValue, final String... owners);

    void unlock(final String achievementId, final String triggerValue, final Collection<String> owners);

    void unlock(final String achievementId, final String triggerValue);

    Boolean isUnlocked(final String achievementId);

    Boolean isUnlocked(String achievementId, Integer level);

    Long getCurrentScore(final String id);

    void reset();
}
