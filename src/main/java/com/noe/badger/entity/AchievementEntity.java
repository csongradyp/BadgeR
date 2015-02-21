package com.noe.badger.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class AchievementEntity {

    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date acquireDate;
    private Integer level;
    private Boolean unlocked;

    public AchievementEntity() {
    }

    public AchievementEntity(final String id) {
        this.id = id;
        level = 1;
        acquireDate = new Date();
        unlocked = true;
    }

    public AchievementEntity(final String id, final Integer level) {
        this.id = id;
        this.level = level;
        acquireDate = new Date();
        unlocked = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public void setAcquireDate(Date acquireDate) {
        this.acquireDate = acquireDate;
    }

    public Boolean getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(Boolean unlocked) {
        this.unlocked = unlocked;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
