package com.noe.badger.entity;

import javax.persistence.Entity;

/**
 * Entity class for counted events.
 */
@Entity
public class Counter extends AbstractEntity {

    private String counterName;
    private Long value;

    public Long incrementValue() {
        return ++value;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(final String counterName) {
        this.counterName = counterName;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(final Long value) {
        this.value = value;
    }

}
