package net.csongradyp.badger.parser.json.domain;

import java.util.List;

public class CompositeAchievementJson implements IAchievementJson {

    private String id;
    private String category;
    private List<String> subscription;
    private String relation;
    private List<String> scoreTrigger;
    private List<RangeTrigger<Long>> scoreRangeTrigger;
    private List<String> dateTrigger;
    private List<String> timeTrigger;
    private List<RangeTrigger<String>> timeRangeTrigger;

    public CompositeAchievementJson() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<String> subscription) {
        this.subscription = subscription;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<String> getScoreTrigger() {
        return scoreTrigger;
    }

    public void setScoreTrigger(List<String> scoreTrigger) {
        this.scoreTrigger = scoreTrigger;
    }

    public List<RangeTrigger<Long>> getScoreRangeTrigger() {
        return scoreRangeTrigger;
    }

    public void setScoreRangeTrigger(List<RangeTrigger<Long>> scoreRangeTrigger) {
        this.scoreRangeTrigger = scoreRangeTrigger;
    }

    public List<String> getDateTrigger() {
        return dateTrigger;
    }

    public void setDateTrigger(List<String> dateTrigger) {
        this.dateTrigger = dateTrigger;
    }

    public List<String> getTimeTrigger() {
        return timeTrigger;
    }

    public void setTimeTrigger(List<String> timeTrigger) {
        this.timeTrigger = timeTrigger;
    }

    public List<RangeTrigger<String>> getTimeRangeTrigger() {
        return timeRangeTrigger;
    }

    public void setTimeRangeTrigger(List<RangeTrigger<String>> timeRangeTrigger) {
        this.timeRangeTrigger = timeRangeTrigger;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
