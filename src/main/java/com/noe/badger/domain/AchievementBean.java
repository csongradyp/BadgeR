package com.noe.badger.domain;

public class AchievementBean implements Achievement {

    private String id;
    private String title;
    private String text;
    private String[] trigger;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String[] getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(String[] trigger) {
        this.trigger = trigger;
    }
}
