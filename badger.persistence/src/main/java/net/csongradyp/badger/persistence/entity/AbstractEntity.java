package net.csongradyp.badger.persistence.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract class for entity DAO-s.
 * Includes the generated ID
 *
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

}
