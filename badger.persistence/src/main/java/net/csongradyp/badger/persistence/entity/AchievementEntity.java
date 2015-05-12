package net.csongradyp.badger.persistence.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AchievementEntity {

    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date acquireDate;
    private Integer level;
    private Boolean unlocked;
    @ElementCollection
    private Set<String> owners;

    public AchievementEntity() {
        owners = new HashSet<>();
    }

    public AchievementEntity(final String id) {
        this();
        this.id = id;
        level = 1;
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

    public Set<String> getOwners() {
        return owners;
    }

    public void setOwners(final Set<String> owners ) {
        this.owners = owners;
    }

    public void addOwners(final Collection<String> owners) {
        this.owners.addAll(owners);
    }
}
