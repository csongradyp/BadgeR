package net.csongradyp.badger.parser;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.IRelationalAchievement;

public class AchievementDefinitionFileStub implements AchievementDefinition {
    @Override
    public Collection<IAchievement> getAll() {
        return null;
    }

    @Override
    public void setRelations(Collection<IRelationalAchievement> relations) {

    }

    @Override
    public void setEvents(String[] events) {

    }

    @Override
    public void setEvents(Collection<String> events) {

    }

    @Override
    public void setAchievements(Collection<IAchievement> achievements) {

    }

    @Override
    public Collection<IAchievement> getAchievementsSubscribedFor(String event) {
        return null;
    }

    @Override
    public Collection<IAchievement> getAchievementsForCategory(String category) {
        return null;
    }

    @Override
    public Collection<IAchievementBean> getDateAchievementsWithoutEvents() {
        return null;
    }

    @Override
    public Map<String, Set<IAchievement>> getAllByEvents() {
        return null;
    }

    @Override
    public IAchievement get(AchievementType type, String id) {
        return null;
    }

    @Override
    public Optional<IAchievement> get(String id) {
        return null;
    }
}
