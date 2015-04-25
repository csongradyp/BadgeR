package com.noe.badger.event.message;

public class Score {

    private String event;
    private Long value;

    public Score(final String event, final Long value) {
        this.event = event;
        this.value = value;
    }

    public String getEvent() {
        return event;
    }

    public Long getValue() {
        return value;
    }
}
