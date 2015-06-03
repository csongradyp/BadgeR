package net.csongradyp.badger.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractAchievementBean implements IAchievementBean {

    private static final String KEY_FORMAT = "%s.%s";
    private String id;
    private String category;
    private List<String> subscriptions;
    private Integer maxLevel;

    public AbstractAchievementBean() {
        subscriptions = new ArrayList<>();
        maxLevel = 1;
        category = "default";
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
        if (category != null && !category.isEmpty()) {
            this.category = category;
        }
    }

    @Override
    public Integer getMaxLevel() {
        return maxLevel;
    }

    @Override
    public void setMaxLevel(final Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public List<String> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void setSubscriptions(final String[] subscriptions) {
        this.subscriptions.addAll(Arrays.asList(subscriptions));
    }

    @Override
    public void setSubscription(final List<String> events) {
        this.subscriptions = events;
    }

    @Override
    public String getTitleKey() {
        return String.format(KEY_FORMAT, id, "title");
    }

    @Override
    public String getTextKey() {
        return String.format(KEY_FORMAT, id, "text");
    }
}
