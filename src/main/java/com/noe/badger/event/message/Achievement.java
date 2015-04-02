package com.noe.badger.event.message;

import java.util.Date;

public class Achievement {

    private final String id;
    private final String title;
    private final String text;
    private final Date acquireDate;
    private final String triggerWith;
    private String category;
    private Integer level;
    private AchievementEventType eventType;

    public Achievement(final String id, final String title, final String text, final String triggerWith) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.acquireDate = new Date();
        this.triggerWith = triggerWith;
        this.level = 1;
    }

    public Achievement(final String id, final String category, final String title, final String text, final String triggerScore) {
        this(id, title, text, triggerScore);
        this.category = category;
    }

    public Achievement(final String id, final String title, final String text, final String triggerScore, final Integer level) {
        this(id, title, text, triggerScore);
        this.level = level;
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
}
