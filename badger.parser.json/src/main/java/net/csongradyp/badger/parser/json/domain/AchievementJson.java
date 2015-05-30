package net.csongradyp.badger.parser.json.domain;

import java.util.List;

public class AchievementJson<T> implements ISimpleTriggerAchievementJson<T> {

    private String id;
    private String category;
    private List<String> subscription;
    private List<T> trigger;

    public AchievementJson() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<String> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<String> subscription) {
        this.subscription = subscription;
    }

    public List<T> getTrigger() {
        return trigger;
    }

    public void setTrigger(List<T> trigger) {
        this.trigger = trigger;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }
}
