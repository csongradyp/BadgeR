package net.csongrady.badger;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.csongrady.badger.domain.AchievementType;
import net.csongrady.badger.domain.IAchievement;
import net.csongrady.badger.domain.IAchievementBean;
import net.csongrady.badger.domain.IRelationalAchievement;

public interface AchievementDefinition {

    Collection<IAchievement> getAll();

    void setRelations(final Collection<IRelationalAchievement> relations);

    void setEvents(String[] events);

    void setEvents(Collection<String> events);

    void setAchievements(Collection<IAchievement> achievements);

    Collection<IAchievement> getAchievementsSubscribedFor(String event);

    Collection<IAchievement> getAchievementsForCategory(String category);

    Collection<IAchievementBean> getDateAchievementsWithoutEvents();

    public Map<String, Set<IAchievement>> getAllByEvents();

    public IAchievement get(AchievementType type, String id);

    public Optional<IAchievement> get(String id);
}
