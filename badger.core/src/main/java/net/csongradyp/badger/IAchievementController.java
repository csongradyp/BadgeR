package net.csongradyp.badger;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import net.csongradyp.badger.domain.IAchievement;

public interface IAchievementController {

    void setAchievementDefinition(AchievementDefinition definition);

    void setInternationalizationBaseName(String internationalizationBaseName);

    void setResourceBundle(ResourceBundle resourceBundle);

    void setLocale(Locale locale);

    Collection<IAchievement> getAll();

    Collection<IAchievement> getAllUnlocked();

    Collection<IAchievement> getAllByOwner(String owner);

    Map<String, Set<IAchievement>> getAllByEvents();

    Optional<IAchievement> get(String id);

    void checkAndUnlock();

    void triggerEventWithHighScore(String event, Long score);

    void triggerEventWithHighScore(String event, Long score, Collection<String> owners);

    void triggerEvent(String event, Long score, Collection<String> owners);

    void triggerEvent(String event, Long score);

    void triggerEvent(String event, Collection<String> owners);

    void triggerEvent(String event);

    void unlock(String achievementId, String triggerValue, Collection<String> owners);

    void unlock(String achievementId, String triggerValue);

    Boolean isUnlocked(String achievementId);

    Boolean isUnlocked(String achievementId, Integer level);

    Long getCurrentScore(String id);

    void reset();
}
