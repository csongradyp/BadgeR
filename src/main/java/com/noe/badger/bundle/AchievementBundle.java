package com.noe.badger.bundle;

import com.noe.badger.AchievementType;
import com.noe.badger.bundle.domain.*;
import com.noe.badger.exception.AchievementNotFoundException;
import com.noe.badger.exception.MalformedAchievementDefinition;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Named;
import org.ini4j.Ini;
import org.ini4j.Profile;

@Named
public class AchievementBundle {

    private Ini achievements;
    private final Map<AchievementType, Map<String, IAchievementBean>> achievementTypeMap;
    private final Map<String, Set<IAchievementBean>> achievementEventMap;
    private final Map<String, Set<IAchievementBean>> achievementCategoryMap;
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

    public void setSource(final File achievementFile) {
        try {
            achievements = new Ini(achievementFile);
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        sortAchievements();
    }

    public void setSource(final String achievementFileLocation) {
        try {
            achievements = new Ini(new File(achievementFileLocation));
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        sortAchievements();
    }

    public void setSource(final URL achievementFile) {
        try {
            achievements = new Ini(achievementFile);
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        sortAchievements();
    }

    public void setSource(final InputStream inputStream) {
        try {
            achievements = new Ini(inputStream);
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        sortAchievements();
    }

    private void sortAchievements() {
        parseRelations();
        setUpEventMap();
        final AchievementType[] types = AchievementType.values();
        for (AchievementType type : types) {
            final Profile.Section typeSection = achievements.get(type.getType());
            if (typeSection != null && typeSection.childrenNames() != null) {
                final String[] AchievementIds = typeSection.childrenNames();
                for (String achievementId : AchievementIds) {
                    IAchievementBean achievementBean;
                    if (!relationMap.containsKey(achievementId)) {
                        achievementBean = parse(achievementId, type, typeSection.getChild(achievementId));
                        addToTypeMap(type, achievementBean);
                    } else {
                        achievementBean = relationMap.get(achievementId);
                    }
                    addToCategoryMap(achievementBean);
                    addToEventMap(achievementBean);
                }
            }
        }
    }

    private void parseRelations() {
        final Profile.Section relationSection = achievements.get("relations");
        if (relationSection != null) {
            for (Map.Entry<String, String> relationEntry : relationSection.entrySet()) {
                final CompositeAchievementBean compositeAchievementBean = new CompositeAchievementBean(relationEntry.getKey(), relationEntry.getValue(), this);
                relationMap.put(compositeAchievementBean.getId(), compositeAchievementBean);
            }
        }
    }

    private void setUpEventMap() {
        final Profile.Section eventSection = achievements.get("events");
        if (eventSection == null || eventSection.childrenNames() == null) {
            throw new MalformedAchievementDefinition("[events] declaration is missing in achievement descriptor ini file!");
        }
        final String[] events = eventSection.getAll("event", String[].class);
        for (String event : events) {
            achievementEventMap.put(event, new HashSet<>());
        }
    }

    public IAchievementBean parse(final AchievementType type, final String achievementId) {
        final Profile.Section typeSection = achievements.get(type.getType());
        if (typeSection != null && typeSection.childrenNames() != null) {
            return parse(achievementId, type, typeSection.getChild(achievementId));
        }
        throw new AchievementNotFoundException(type, achievementId);
    }

    private IAchievementBean parse(final String id, final AchievementType type, final Profile.Section section) {
        if (section != null) {
            IAchievementBean achievementBean = null;
            switch (type) {
                case DATE:
                    achievementBean = parseSection(id, section, new DateAchievementBean());
                    break;
                case TIME:
                    achievementBean = parseSection(id, section, new TimeAchievementBean());
                    break;
                case TIME_RANGE:
                    achievementBean = parseSection(id, section, new TimeRangeAchievementBean());
                    break;
                case COUNTER:
                    achievementBean = parseSection(id, section, new CounterAchievementBean());
                    break;
                case SINGLE:
                    achievementBean = parseSection(id, section, new CounterAchievementBean());
                    break;
            }
            return achievementBean;
        }
        throw new AchievementNotFoundException(type, id);
    }

    private IAchievementBean parseSection(final String id, final Profile.Section section, final IAchievementBean achievement) {
        section.to(achievement);
        achievement.setId(id);
        final String[] triggers = section.getAll("trigger", String[].class);
        achievement.setTrigger(triggers);
        final String[] events = section.getAll("event", String[].class);
        achievement.setEvent(events);
        return achievement;
    }

    private void addToEventMap(final IAchievementBean achievementBean) {
        final List<String> achievementEventSubscriptions = achievementBean.getEvent();
        if (achievementEventSubscriptions != null && !achievementEventSubscriptions.isEmpty()) {
            achievementEventSubscriptions.forEach(achievementEventSubscription -> achievementEventMap.get(achievementEventSubscription).add(achievementBean));
        }
    }

    private void addToCategoryMap(final IAchievementBean achievementBean) {
        final String category = achievementBean.getCategory();
        if (achievementCategoryMap.get(category) == null) {
            achievementCategoryMap.put(category, new HashSet<>());
        }
        achievementCategoryMap.get(category).add(achievementBean);
    }

    private void addToTypeMap(final AchievementType type, final IAchievementBean achievementBean) {
        achievementTypeMap.get(type).put(achievementBean.getId(), achievementBean);
    }

    public Collection<IAchievementBean> getAchievementsSubscribedFor(final String event) {
        return achievementEventMap.get(event);
    }

    public Collection<IAchievementBean> getAchievementsForCategory(final String category) {
        return achievementCategoryMap.get(category);
    }

    public Collection<DateAchievementBean> getDateAchievementsWithoutEvents() {
        final Map<String, IAchievementBean> dateAchievements = achievementTypeMap.get(AchievementType.DATE);
        return (dateAchievements.values().stream()
                .filter(achievementBean -> achievementBean.getEvent() == null || achievementBean.getEvent().isEmpty())
                .map(achievementBean -> (DateAchievementBean) achievementBean)
                .collect(Collectors.toList()));
    }

    public Collection<IAchievementBean> getAll() {
        Collection<IAchievementBean> allAchievements = new HashSet<>();
        achievementEventMap.values().forEach(allAchievements::addAll);
        relationMap.values().forEach(allAchievements::add);
        return allAchievements;
    }

    public Map<String, Set<IAchievementBean>> getAllByEvents() {
        return achievementEventMap;
    }

    public IAchievementBean get(final AchievementType type, final String id) {
        final Map<String, IAchievementBean> achievementBeanMap = achievementTypeMap.get(type);
        if (achievementBeanMap.containsKey(id)) {
            return achievementBeanMap.get(id);
        }
        throw new AchievementNotFoundException(type, id);
    }

}
