package net.csongradyp.badger;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.IRelationalAchievement;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.exception.AchievementNotFoundException;

public class AchievementBundle implements AchievementDefinition {

    private final Map<AchievementType, Map<String, IAchievement>> achievementTypeMap;
    private final Map<String, Set<IAchievement>> achievementEventMap;
    private final Map<String, Set<IAchievement>> achievementCategoryMap;
    private final Map<String, IRelationalAchievement> relationMap;

    public AchievementBundle() {
        achievementTypeMap = new HashMap<>();
        achievementEventMap = new HashMap<>();
        achievementCategoryMap = new HashMap<>();
        relationMap = new HashMap<>();
        setUpTypeMap();
    }

    private void setUpTypeMap() {
        for (AchievementType type : AchievementType.values()) {
            achievementTypeMap.put(type, new HashMap<>());
        }
    }

    @Override
    public void setEvents(final String[] events) {
        for (String event : events) {
            achievementEventMap.put(event, new HashSet<>());
        }
    }

    @Override
    public void setEvents(final Collection<String> events) {
        events.stream().forEach(event ->  achievementEventMap.put(event, new HashSet<>()));
    }

    @Override
    public void setAchievements(Collection<IAchievement> achievements) {
        achievements.stream().forEach(this::add);
    }

    private void add(final IAchievement achievement) {
        final String id = achievement.getId();
        final AchievementType type = achievement.getType();
        if (relationMap.containsKey(id)) {
            relationMap.get(id).addChild(type, achievement);
            final IAchievement compositeAchievement = relationMap.get(id);
            addToSortedContainers(compositeAchievement, AchievementType.COMPOSITE);
        } else {
            addToSortedContainers(achievement, type);
        }
    }

    private void addToSortedContainers(IAchievement achievement, AchievementType type) {
        addToTypeMap(type, achievement);
        addToCategoryMap(achievement);
        addToEventMap(achievement);
    }

    private void addToTypeMap(final AchievementType type, final IAchievement achievementBean) {
        achievementTypeMap.get(type).put(achievementBean.getId(), achievementBean);
    }

    private void addToEventMap(final IAchievement achievementBean) {
        final List<String> eventSubscriptions = achievementBean.getEvent();
        if (eventSubscriptions != null && !eventSubscriptions.isEmpty()) {
            eventSubscriptions.forEach(achievementEventSubscription -> {
                achievementEventMap.get(achievementEventSubscription).add(achievementBean);
            });
        }
    }

    private void addToCategoryMap(final IAchievement achievementBean) {
        final String category = achievementBean.getCategory();
        if (achievementCategoryMap.get(category) == null) {
            achievementCategoryMap.put(category, new HashSet<>());
        }
        achievementCategoryMap.get(category).add(achievementBean);
    }

    @Override
    public void setRelations(final Collection<IRelationalAchievement> relations) {
        relations.stream().forEach(compositeBean -> {
            relationMap.put(compositeBean.getId(), compositeBean);
            compositeBean.getChildren().keySet().forEach(type -> addToTypeMap(type, compositeBean));
            addToEventMap(compositeBean);
        });
    }

    @Override
    public Collection<IAchievement> getAchievementsSubscribedFor(final String event) {
        return achievementEventMap.get(event);
    }

    @Override
    public Collection<IAchievement> getAchievementsForCategory(final String category) {
        return achievementCategoryMap.get(category);
    }

    @Override
    public Collection<IAchievementBean> getDateAchievementsWithoutEvents() {
        final Map<String, IAchievement> dateAchievements = achievementTypeMap.get(AchievementType.DATE);
        return (dateAchievements.values().stream()
                .filter(achievementBean -> achievementBean.getEvent() == null || achievementBean.getEvent().isEmpty())
                .map(achievementBean -> (DateAchievementBean) achievementBean)
                .collect(Collectors.toList()));
    }

    @Override
    public Collection<IAchievement> getAll() {
        Collection<IAchievement> allAchievements = new HashSet<>();
        achievementTypeMap.values().forEach(achievementMap -> allAchievements.addAll(achievementMap.values()));
        relationMap.values().forEach(allAchievements::add);
        return allAchievements;
    }

    @Override
    public Map<String, Set<IAchievement>> getAllByEvents() {
        return achievementEventMap;
    }

    @Override
    public IAchievement get(final AchievementType type, final String id) {
        final Map<String, IAchievement> achievementBeanMap = achievementTypeMap.get(type);
        if (achievementBeanMap.containsKey(id)) {
            return achievementBeanMap.get(id);
        }
        throw new AchievementNotFoundException(type, id);
    }

    @Override
    public Optional<IAchievement> get(final String id) {
        return getAll().stream().filter(achievement -> achievement.getId().equals(id)).findAny();
    }

}
