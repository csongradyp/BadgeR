package com.noe.badger.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class for counted events.
 */
@Entity
public class Counter {

    @Id
    private String name;
    private Long value;

    public Counter() {
    }

    public Long incrementValue() {
        return ++value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(final Long value) {
        this.value = value;
    }

}
