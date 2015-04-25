package com.noe.badger.event.message;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Achievement {

    private final String id;
    private final String title;
    private final String text;
    private final Date acquireDate;
    private final String triggerWith;
    private String category;
    private Integer level;
    private AchievementEventType eventType;
    private Set<String> owners;

    public Achievement(final String id, final String title, final String text, final String triggerWith) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.acquireDate = new Date();
        this.triggerWith = triggerWith;
        this.level = 1;
        owners = new HashSet<>();
        eventType = AchievementEventType.UNLOCK;
    }

    public Achievement(final String id, final String category, final String title, final String text, final String triggerScore) {
        this(id, title, text, triggerScore);
        this.category = category;
        eventType = AchievementEventType.UNLOCK;
    }

    public Achievement(final String id, final String title, final String text, final String triggerScore, final Integer level) {
        this(id, title, text, triggerScore);
        this.level = level;
        if(level > 1) {
            eventType = AchievementEventType.LEVEL_UP;
        }
    }

    public Achievement(final String id, final String category, final String title, final String text, final String triggerScore, final Integer level) {
        this(id, title, text, triggerScore, level);
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public String getTriggerWith() {
        return triggerWith;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }

    public AchievementEventType getEventType() {
        return eventType;
    }

    public void setEventType(final AchievementEventType eventType) {
        this.eventType = eventType;
    }

    public Set<String> getOwners() {
        return owners;
    }

    public void setOwners(final Set<String> owners) {
        this.owners = owners;
    }

    public void addOwners(final Collection<String> owners) {
        this.owners.addAll(owners);
    }

    public void addOwners(final String... owners) {
        Collections.addAll(this.owners, owners);
    }
}
