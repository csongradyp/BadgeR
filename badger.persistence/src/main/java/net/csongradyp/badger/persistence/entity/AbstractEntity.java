package net.csongradyp.badger.persistence.entity;

import javax.persistence.GeneratedValue;
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
    @GeneratedValue
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
