package net.csongradyp.badger;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;

import java.util.*;

public class AchievementBundle implements AchievementDefinition {

    private final Map<AchievementType, Map<String, IAchievement>> achievementTypeMap;
    private final Map<String, Set<IAchievement>> achievementEventMap;
    private final Map<String, Set<IAchievement>> achievementCategoryMap;

    public AchievementBundle() {
        achievementTypeMap = new HashMap<>();
        achievementEventMap = new HashMap<>();
        achievementCategoryMap = new HashMap<>();
        setUpTypeMap();
    }

    private void setUpTypeMap() {
        for (AchievementType type : AchievementType.values()) {
            achievementTypeMap.put(type, new HashMap<>());
        }
    }

    @Override
    public void setEvents(final String[] events) {
        setEvents(Arrays.asList(events));
    }

    @Override
    public void setEvents(final Collection<String> events) {
        events.stream().forEach(event -> achievementEventMap.put(event, new HashSet<>()));
    }

    @Override
    public void setAchievements(Collection<IAchievement> achievements) {
        achievements.stream().forEach(this::add);
    }

    private void add(final IAchievement achievement) {
        addToTypeMap(achievement.getType(), achievement);
        addToCategoryMap(achievement);
        addToEventMap(achievement);
    }

    private void addToTypeMap(final AchievementType type, final IAchievement achievementBean) {
        achievementTypeMap.get(type).put(achievementBean.getId(), achievementBean);
    }

    private void addToEventMap(final IAchievement achievementBean) {
        final List<String> eventSubscriptions = achievementBean.getSubscriptions();
        if (eventSubscriptions != null && !eventSubscriptions.isEmpty()) {
            eventSubscriptions.forEach(event -> {
                if(!achievementEventMap.containsKey(event)) {
                    throw new MalformedAchievementDefinition("Event declaration is missing for event: " + event);
                }
                achievementEventMap.get(event).add(achievementBean);
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
    public Collection<IAchievement> getAchievementsSubscribedFor(final String event) {
        return achievementEventMap.get(event);
    }

    @Override
    public Collection<IAchievement> getAchievementsForCategory(final String category) {
        return achievementCategoryMap.get(category);
    }

    @Override
    public Collection<IAchievement> getAll() {
        Collection<IAchievement> allAchievements = new HashSet<>();
        achievementTypeMap.values().forEach(achievementMap -> allAchievements.addAll(achievementMap.values()));
        return allAchievements;
    }

    @Override
    public Map<String, Set<IAchievement>> getAllByEvents() {
        return achievementEventMap;
    }

    @Override
    public Optional<IAchievement> get(final AchievementType type, final String id) {
        Optional<IAchievement> achievement = Optional.empty();
        final Map<String, IAchievement> achievementBeanMap = achievementTypeMap.get(type);
        if (achievementBeanMap.containsKey(id)) {
            achievement = Optional.of(achievementBeanMap.get(id));
        }
        return achievement;
    }

    @Override
    public Optional<IAchievement> get(final String id) {
        return getAll().stream().filter(achievement -> achievement.getId().equals(id)).findAny();
    }

}
