package com.noe.badger.event.message;

import java.util.*;

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
        category = "default";
    }

    public Achievement(final String id, final String category, final String title, final String text, final String triggerScore) {
        this(id, title, text, triggerScore);
        this.category = category;
        eventType = AchievementEventType.UNLOCK;
    }

    public Achievement(final String id, final String title, final String text, final String triggerScore, final Integer level) {
        this(id, title, text, triggerScore);
        this.level = level;
        category = "default";
        if (level > 1) {
            eventType = AchievementEventType.LEVEL_UP;
        }
    }

    public Achievement(final String id, final String category, final String title, final String text, final String triggerScore, final Integer level) {
        this(id, title, text, triggerScore, level);
        this.category = category;
    }

    /**
     * Returns the ID of the unlocked achievement.
     *
     * @return Unique ID of the achievement defined in the achievement definition file.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the category of the achievement. Default value is {@code "default"}.
     *
     * @return Category of the unlocked achievement.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the localized title of the achievement.
     * If no internationalization file is given it will return the localization key ({@code ACHIEVEMENT_ID.title}).
     *
     * @return Localized message or the i18n key of the achievement title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the localized description of the achievement.
     * If no internationalization file is given it will return the localization key ({@code ACHIEVEMENT_ID.text}).
     *
     * @return Localized message or the i18n key of the achievement description.
     */
    public String getText() {
        return text;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public String getTriggerWith() {
        return triggerWith;
    }

    /**
     * Returns the current unlocked level of the achievement.
     *
     * @return Unlocked level of the achievement.
     */
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
