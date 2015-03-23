package com.noe.badger.bundle.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractAchievementBean<T> implements IAchievementBean<T> {

    private String id;
    private List<String> event;
    private String titleKey;
    private String textKey;
    private Integer maxLevel;

    public AbstractAchievementBean() {
        event = new ArrayList<>();
        maxLevel = 1;
    }

    @Override
    public Integer getMaxLevel() {
        return maxLevel;
    }

    @Override
    public void setMaxLevel( Integer maxLevel ) {
        this.maxLevel = maxLevel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<String> getEvent() {
        return event;
    }

    @Override
    public void setEvent(String[] event) {
        Collections.addAll(this.event, event);
    }

    @Override
    public String getTitleKey() {
        return titleKey;
    }

    @Override
    public void setTitleKey( String titleKey ) {
        this.titleKey = titleKey;
    }

    @Override
    public String getTextKey() {
        return textKey;
    }

    @Override
    public void setTextKey( String textKey ) {
        this.textKey = textKey;
    }

    @Override public String toString() {
        return "Achievement {" +
               "id='" + id + '\'' +
               ", titleKey='" + titleKey + '\'' +
               ", textKey='" + textKey + '\'';
    }
}
