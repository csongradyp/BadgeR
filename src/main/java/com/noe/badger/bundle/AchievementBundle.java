package com.noe.badger.bundle;

import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.bundle.domain.achievement.AchievementType;
import com.noe.badger.bundle.domain.achievement.CompositeAchievementBean;
import com.noe.badger.bundle.domain.achievement.DateAchievementBean;
import com.noe.badger.exception.AchievementNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AchievementBundle {

    private final Map<AchievementType, Map<String, IAchievement>> achievementTypeMap;
    private final Map<String, Set<IAchievement>> achievementEventMap;
    private final Map<String, Set<IAchievement>> achievementCategoryMap;
    private final Map<String, CompositeAchievementBean> relationMap;

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

    public void setEvents(final String[] events) {
        for (String event : events) {
            achievementEventMap.put(event, new HashSet<>());
        }
    }

    public void setEvents(final Collection<String> events) {
        events.stream().forEach(event ->  achievementEventMap.put(event, new HashSet<>()));
    }

    public void setAchievements(Collection<IAchievement> achievements) {
        achievements.stream().forEach(this::add);
    }

    private void add(final IAchievement achievement) {
        final String id = achievement.getId();
        final AchievementType type = achievement.getType();
        if (relationMap.containsKey(id)) {
            relationMap.get(id).addWrappedElement(type, achievement);
            IAchievement compositeAchievement = relationMap.get(id);
            addToCategoryMap(compositeAchievement);
            addToEventMap(compositeAchievement);
        } else {
            addToTypeMap(type, achievement);
            addToCategoryMap(achievement);
            addToEventMap(achievement);
        }
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

    public void setRelations(final Collection<CompositeAchievementBean> relations) {
        relations.stream().forEach(compositeBean -> {
            relationMap.put(compositeBean.getId(), compositeBean);
            compositeBean.getChildren().keySet().forEach(type -> addToTypeMap(type, compositeBean));
            addToEventMap(compositeBean);
        });
    }

    public Collection<IAchievement> getAchievementsSubscribedFor(final String event) {
        return achievementEventMap.get(event);
    }

    public Collection<IAchievement> getAchievementsForCategory(final String category) {
        return achievementCategoryMap.get(category);
    }

    public Collection<DateAchievementBean> getDateAchievementsWithoutEvents() {
        final Map<String, IAchievement> dateAchievements = achievementTypeMap.get(AchievementType.DATE);
        return (dateAchievements.values().stream()
                .filter(achievementBean -> achievementBean.getEvent() == null || achievementBean.getEvent().isEmpty())
                .map(achievementBean -> (DateAchievementBean) achievementBean)
                .collect(Collectors.toList()));
    }

    public Collection<IAchievement> getAll() {
        Collection<IAchievement> allAchievements = new HashSet<>();
        achievementTypeMap.values().forEach(achievementMap -> allAchievements.addAll(achievementMap.values()));
        relationMap.values().forEach(allAchievements::add);
        return allAchievements;
    }

    public Map<String, Set<IAchievement>> getAllByEvents() {
        return achievementEventMap;
    }

    public IAchievement get(final AchievementType type, final String id) {
        final Map<String, IAchievement> achievementBeanMap = achievementTypeMap.get(type);
        if (achievementBeanMap.containsKey(id)) {
            return achievementBeanMap.get(id);
        }
        throw new AchievementNotFoundException(type, id);
    }

    public Optional<IAchievement> get(final String id) {
        return getAll().stream().filter(achievement -> achievement.getId().equals(id)).findAny();
    }

}
