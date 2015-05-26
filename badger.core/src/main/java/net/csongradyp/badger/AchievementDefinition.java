package net.csongradyp.badger;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AchievementDefinition {

    Collection<IAchievement> getAll();

    void setEvents(String[] events);

    void setEvents(Collection<String> events);

    void setAchievements(Collection<IAchievement> achievements);

    Collection<IAchievement> getAchievementsSubscribedFor(String event);

    Collection<IAchievement> getAchievementsForCategory(String category);

    Map<String, Set<IAchievement>> getAllByEvents();

    Optional<IAchievement> get(AchievementType type, String id);

    Optional<IAchievement> get(String id);
}
