package net.csongradyp.badger;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;

public interface IAchievementController {

    void setDefinition(final AchievementDefinition definition);

    void setInternationalizationBaseName(final String internationalizationBaseName);

    void setResourceBundle(final ResourceBundle resourceBundle);

    void setLocale(final Locale locale);

    Collection<IAchievement> getAll();

    Collection<IAchievement> getByOwner(final String owner);

    IAchievement get(final AchievementType type, final String id);

    Map<String, Set<IAchievement>> getAllByEvents();

    void checkAll();

    void triggerEventWithHighScore(final String event, final Long score);

    void triggerEvent(final String event, final Long score);

    void triggerEvent(final String event, final String... owners);

    void triggerEvent(final String event, final Collection<String> owners);

    void triggerEvent(final String event);

    Optional<IAchievementUnlockedEvent> unlockable(final Long currentValue, final IAchievement achievementBean);

    void unlock(final AchievementType type, final String achievementId, String triggeredValue);

    void unlock(final AchievementType type, final String achievementId, final String triggeredValue, final String... owners);

    void unlock(final String achievementId, final String triggerValue, final Collection<String> owners);

    void unlock(final String achievementId, final String triggerValue);

    Boolean isUnlocked(final String achievementId);

    Long getCurrentScore(final String id);

    void reset();
}
