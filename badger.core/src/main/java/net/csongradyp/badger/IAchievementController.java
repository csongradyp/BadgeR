package net.csongradyp.badger;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import net.csongradyp.badger.domain.IAchievement;

public interface IAchievementController {

    void setAchievementDefinition(final AchievementDefinition definition);

    void setInternationalizationBaseName(final String internationalizationBaseName);

    void setResourceBundle(final ResourceBundle resourceBundle);

    void setLocale(final Locale locale);

    Collection<IAchievement> getAll();

    Collection<IAchievement> getAllUnlocked();

    Collection<IAchievement> getAllByOwner(final String owner);

    Map<String, Set<IAchievement>> getAllByEvents();

    Optional<IAchievement> get(String id);

    void checkAndUnlock();

    void triggerEventWithHighScore(final String event, final Long score);

    void triggerEvent(final String event, final Long score);

    void triggerEvent(final String event, final Collection<String> owners);

    void triggerEvent(final String event);

    void unlock(final String achievementId, final String triggerValue, final Collection<String> owners);

    void unlock(final String achievementId, final String triggerValue);

    Boolean isUnlocked(final String achievementId);

    Boolean isUnlocked(String achievementId, Integer level);

    Long getCurrentScore(final String id);

    void reset();
}
