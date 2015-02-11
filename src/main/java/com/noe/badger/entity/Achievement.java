package com.noe.badger.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Achievement {

    @Id
    private String id;
    private String title;
    @Temporal(TemporalType.TIMESTAMP)
    private Date acquireDate;
    private Boolean unlocked;

    public Achievement() {
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Getter for property 'title'.
     *
     * @return Value for property 'title'.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for property 'title'.
     *
     * @param title Value to set for property 'title'.
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * Getter for property 'acquireDate'.
     *
     * @return Value for property 'acquireDate'.
     */
    public Date getAcquireDate() {
        return acquireDate;
    }

    /**
     * Setter for property 'acquireDate'.
     *
     * @param acquireDate Value to set for property 'acquireDate'.
     */
    public void setAcquireDate( Date acquireDate ) {
        this.acquireDate = acquireDate;
    }

    /**
     * Getter for property 'unlocked'.
     *
     * @return Value for property 'unlocked'.
     */
    public Boolean getUnlocked() {
        return unlocked;
    }

    /**
     * Setter for property 'unlocked'.
     *
     * @param unlocked Value to set for property 'unlocked'.
     */
    public void setUnlocked( Boolean unlocked ) {
        this.unlocked = unlocked;
    }
}
