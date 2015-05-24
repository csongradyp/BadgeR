package net.csongradyp.badger.domain;

public interface IAchievementBean extends IAchievement {

    String PROP_ID = "id";
    String PROP_CATEGORY = "category";
    String PROP_EVENT = "event";
    String PROP_MAX_LEVEL = "maxLevel";

    void setId(String id);

    void setCategory(String category);

    void setEvent(String[] event);

    void setMaxLevel(Integer maxLevels);
}
