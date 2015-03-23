package com.noe.badger.bundle;

import com.noe.badger.AchievementType;
import com.noe.badger.bundle.domain.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javax.inject.Named;
import org.ini4j.Ini;
import org.ini4j.Profile;

@Named
public class AchievementBundle {

    private Ini achievements;
    private Map<String, Set<IAchievementBean>> achievementEventMap = new HashMap<>();

    public void setSource(final File achievementFile) {
        try {
            achievements = new Ini(achievementFile);
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
        sortAchievementsByEvents();
    }

    public void setSource(final String achievementFileLocation) {
        try {
            achievements = new Ini(new File(achievementFileLocation));
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
        sortAchievementsByEvents();
    }

    public void setSource(final URL achievementFile) {
        try {
            achievements = new Ini(achievementFile);
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
        sortAchievementsByEvents();
    }

    public void setSource(final InputStream inputStream) {
        try {
            achievements = new Ini(inputStream);
            achievements.getConfig().setMultiOption(true);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
        sortAchievementsByEvents();
    }

    private void sortAchievementsByEvents() {
        setUpEventMap();
        final AchievementType[] types = AchievementType.values();
        for (AchievementType type : types) {
            final Profile.Section typeSection = achievements.get(type.getType());
            if (typeSection != null && typeSection.childrenNames() != null) {
                final String[] AchievementIds = typeSection.childrenNames();
                for (String achievementId : AchievementIds) {
                    final IAchievementBean achievementBean = getAchievement(achievementId, type, typeSection.getChild(achievementId));
                    final List<String> achievementEventSubscriptions = achievementBean.getEvent();
                    if(achievementEventSubscriptions != null && !achievementEventSubscriptions.isEmpty()) {
                        achievementEventSubscriptions.forEach(achievementEventSubscription -> achievementEventMap.get(achievementEventSubscription).add(achievementBean));
                    }
                }
            }
        }
    }

    private void setUpEventMap() {
        final Profile.Section eventSection = achievements.get("events");
        if(eventSection == null || eventSection.childrenNames() == null) {
            throw new RuntimeException("Event declaration is missing in achievement descriptor file!");
        }
        final String[] events = eventSection.getAll("event", String[].class);
        for (String event : events) {
            final Set<IAchievementBean> achievementBeans = new HashSet<>();
            achievementEventMap.put(event, achievementBeans);
        }
    }

    public Collection<IAchievementBean> getAchievementsSubscribedFor(final String event) {
        return achievementEventMap.get(event);
    }

    public Collection<DateAchievementBean> getDateAchievementsWithoutEvents() {
        final Collection<DateAchievementBean> dateAchievementBeans = new ArrayList<>();
        Ini.Section dateSection = achievements.get(AchievementType.DATE);
        if (dateSection != null && dateSection.childrenNames() != null) {
            final String[] AchievementIds = dateSection.childrenNames();
            for (String achievementId : AchievementIds) {
                final DateAchievementBean achievementBean = (DateAchievementBean) getAchievement(achievementId, AchievementType.DATE, dateSection.getChild(achievementId));
                if(achievementBean.getEvent() == null || achievementBean.getEvent().isEmpty()) {
                    dateAchievementBeans.add(achievementBean);
                }
            }
        }
        return dateAchievementBeans;
    }

    public IAchievementBean<Long> getCounterAchievement(final String id) {
        return getCounterAchievement(AchievementType.COUNTER, id);
    }

    public List<IAchievementBean<Long>> getCounterAchievementForCounter(final String counter) {
        return getCounterAchievements(counter);
    }

    private List<IAchievementBean<Long>> getCounterAchievements(String counter) {
        List<IAchievementBean<Long>> achievementList = new ArrayList<>();
        final String[] childrenNames = achievements.get(AchievementType.COUNTER.getType()).childrenNames();
        for (String childName : childrenNames) {
            final IAchievementBean achievement = getAchievement(childName);
        }
        return achievementList;
    }

    public IAchievementBean<Long> getSingleAchievement(final String id) {
        return getCounterAchievement(AchievementType.SINGLE, id);
    }

    public IAchievementBean<String> getDateAchievement(final String id) {
        return getDateAchievement(AchievementType.DATE, id);
    }

    public IAchievementBean<String> getTimeAchievement(final String id) {
        return getDateAchievement(AchievementType.TIME, id);
    }

    public IAchievementBean<Long> getCounterAchievement(final AchievementType type, final String id) {
        Ini.Section section = achievements.get(type.getType()).getChild(id);
        if (section != null) {
            final IAchievementBean<Long> counterAchievement = new CounterAchievementBean();
            return parseSection(id, section, counterAchievement);
        }
        return null;
    }

    public IAchievementBean<String> getDateAchievement(final AchievementType type, final String id) {
        Ini.Section section = achievements.get(type.getType()).getChild(id);
        if (section != null) {
            final IAchievementBean<String> counterAchievement = new DateAchievementBean();
            return parseSection(id, section, counterAchievement);
        }
        return null;
    }

    public IAchievementBean<TimeRangeAchievementBean.TimeTriggerPair> getTimeRangeAchievement(final String id) {
        Ini.Section section = achievements.get(AchievementType.TIME_RANGE.getType()).getChild(id);
        if (section != null) {
            final IAchievementBean<TimeRangeAchievementBean.TimeTriggerPair> counterAchievement = new TimeRangeAchievementBean();
            return parseSection(id, section, counterAchievement);
        }
        return null;
    }

    public IAchievementBean getAchievement(final String id) {
        final AchievementType[] types = AchievementType.values();
        for (AchievementType type : types) {
            final Profile.Section section = achievements.get(type.getType()).getChild(id);
            getAchievement(id, type, section);
        }
        return null;
    }

    private IAchievementBean getAchievement(final String id, final AchievementType type, final Profile.Section section) {
        if (section != null) {
            IAchievementBean achievementBean = null;
            switch (type) {
                case DATE:
                    achievementBean = parseSection(id, section, new DateAchievementBean());
                    break;
                case TIME:
                    achievementBean =  parseSection(id, section, new TimeAchievementBean());
                    break;
                case COUNTER:
                    achievementBean =  parseSection(id, section, new CounterAchievementBean());
                    break;
                case SINGLE:
                    achievementBean =  parseSection(id, section, new CounterAchievementBean());
                    break;
            }
            return achievementBean;
        }
        throw new RuntimeException("Achievement not found with type:" + type.getType() + " id:" + id);
    }

    private IAchievementBean parseSection(String id, Profile.Section section, IAchievementBean achievement) {
        section.to(achievement);
        achievement.setId(id);
        final String[] triggers = section.getAll("trigger", String[].class);
        achievement.setTrigger(triggers);
        final String[] events = section.getAll("event", String[].class);
        achievement.setEvent(events);
        return achievement;
    }
}
