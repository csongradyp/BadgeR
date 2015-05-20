package net.csongradyp.badger.persistence.entity;

import javax.persistence.Entity;

/**
 * Entity class for counted events.
 */
@Entity
public class EventEntity extends AbstractEntity {

    private Long score;

    public EventEntity() {
        score = Long.MIN_VALUE;
    }

    public Long incrementScore() {
        return ++score;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(final Long value) {
        this.score = value;
    }

}
