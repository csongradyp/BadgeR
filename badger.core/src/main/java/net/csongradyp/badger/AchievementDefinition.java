package net.csongradyp.badger;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;

public interface AchievementDefinition {

    Collection<IAchievement> getAll();

    void setEvents(String[] events);

    void setEvents(Collection<String> events);

    void setAchievements(Collection<IAchievement> achievements);

    Collection<IAchievement> getAchievementsSubscribedFor(String event);

    Collection<IAchievement> getAchievementsForCategory(String category);

    public Map<String, Set<IAchievement>> getAllByEvents();

    public IAchievement get(AchievementType type, String id);

    public Optional<IAchievement> get(String id);
}
