package net.csongradyp.badger.domain;

import java.util.List;

public interface IAchievementBean extends IAchievement {

    String PROP_ID = "id";
    String PROP_CATEGORY = "category";
    String PROP_EVENT = "subscription";
    String PROP_MAX_LEVEL = "maxLevel";

    void setId(String id);

    void setCategory(String category);

    void setSubscriptions(String[] event);

    void setSubscription(List<String> events);

    void setMaxLevel(Integer maxLevels);
}
