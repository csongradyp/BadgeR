package net.csongradyp.badger.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractAchievementBean implements IAchievementBean {

    private String id;
    private String category;
    private List<String> subscriptions;
    private Integer maxLevel;

    public AbstractAchievementBean() {
        subscriptions = new ArrayList<>();
        maxLevel = 1;
        category= "default";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(final String category) {
        if(category != null && !category.isEmpty()) {
            this.category = category;
        }
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
    public List<String> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void setSubscriptions(String[] subscriptions) {
        Collections.addAll(this.subscriptions, subscriptions);
    }

    @Override
    public void setSubscription(List<String> events) {
        this.subscriptions = events;
    }

    @Override
    public String getTitleKey() {
        return String.format("%s.%s", id, "title");
    }

    @Override
    public String getTextKey() {
        return String.format("%s.%s", id, "text");
    }

    @Override
    public String toString() {
        return "AchievementBean {" +
               "id='" + id + '\'' +
                ", type='" + getType() + '\'' +
                ", category='" + category + '\'' +
               '}';
    }
}
