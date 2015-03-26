package com.noe.badger.bundle.domain;

import java.util.List;

public interface IAchievementBean<T> {

    String PROP_ID = "id";
    String PROP_EVENT = "event";
    String PROP_TRIGGER = "trigger";
    String PROP_MAX_LEVEL = "maxLevel";

    String getId();

    void setId(String id);

    List<String> getEvent();

    void setEvent(String[] event);

    String getTitleKey();

    String getTextKey();

    List<T> getTrigger();

    void setTrigger(String[] trigger);

    Integer getMaxLevel();

    void setMaxLevel(Integer maxLevels);
}
