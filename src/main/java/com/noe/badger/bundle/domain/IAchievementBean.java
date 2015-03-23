package com.noe.badger.bundle.domain;

import java.util.List;

public interface IAchievementBean<T> {

    String PROP_ID = "id";
    String PROP_EVENT = "event";
    String PROP_TITLE_KEY = "titleKey";
    String PROP_TEXT_KEY = "textKey";
    String PROP_TRIGGER = "trigger";
    String PROP_MAX_LEVEL = "maxLevel";

    String getId();

    void setId(String id);

    List<String> getEvent();

    void setEvent(String[] event);

    String getTitleKey();

    void setTitleKey(String title);

    String getTextKey();

    void setTextKey(String text);

    List<T> getTrigger();

    void setTrigger(String[] trigger);

    Integer getMaxLevel();

    void setMaxLevel(Integer maxLevels);
}
