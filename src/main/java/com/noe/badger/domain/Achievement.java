package com.noe.badger.domain;

import java.util.Date;

public class Achievement {

    private String title;
    private String text;
    private Date acquireDate;
    private Integer level;
    private Long triggerValue;

    public Achievement(final String title, final String text, final Date acquireDate, final Integer level) {
        this.title = title;
        this.text = text;
        this.acquireDate = acquireDate;
        this.level = level;
    }

    public Achievement(final String title, final String text, final Date acquireDate) {
        this.title = title;
        this.text = text;
        this.acquireDate = acquireDate;
        this.level = 1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public void setAcquireDate(final Date acquireDate) {
        this.acquireDate = acquireDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }
}
