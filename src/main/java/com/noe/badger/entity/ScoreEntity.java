package com.noe.badger.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class for counted events.
 */
@Entity
public class ScoreEntity {

    @Id
    private String name;
    private Long score;

    public ScoreEntity() {
    }

    public ScoreEntity( final String name ) {
        this.name = name;
        score = 1L;
    }

    public ScoreEntity( final String name, final Long score ) {
        this.name = name;
        this.score = score;
    }

    public Long incrementScore() {
        return ++score;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(final Long value) {
        this.score = value;
    }

}
