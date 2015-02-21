package com.noe.badger.event.domain;

import java.util.Date;

public class Achievement {

    private final String title;
    private final String text;
    private final Date acquireDate;
    private final String triggerWith;
    private Integer level;
    private AchievementEventType eventType;

    public Achievement(final String title, final String text, final Date acquireDate, final String triggerWith, final Integer level) {
        this.title = title;
        this.text = text;
        this.acquireDate = acquireDate;
        this.triggerWith = triggerWith;
        this.level = level;
    }

    public Achievement(final String title, final String text, final Date acquireDate, final String triggerWith) {
        this.title = title;
        this.text = text;
        this.acquireDate = acquireDate;
        this.triggerWith = triggerWith;
        this.level = 1;
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
}