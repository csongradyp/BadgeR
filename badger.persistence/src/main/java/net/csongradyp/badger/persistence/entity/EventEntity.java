package net.csongradyp.badger.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class for counted events.
 */
@Entity
public class EventEntity {

    @Id
    private String id;
    private Long score;

    public EventEntity() {
    }

    public EventEntity(final String id) {
        this.id = id;
        score = 1L;
    }

    public EventEntity(final String id, final Long score) {
        this.id = id;
        this.score = score;
    }

    public Long incrementScore() {
        return ++score;
    }

    public String getId() {
        return id;
    }

    public void setId(final String name) {
        this.id = name;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(final Long value) {
        this.score = value;
    }

}