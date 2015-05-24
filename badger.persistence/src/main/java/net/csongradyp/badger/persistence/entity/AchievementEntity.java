package net.csongradyp.badger.persistence.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AchievementEntity extends AbstractEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date acquireDate;
    private Integer level;
    @ElementCollection
    private Set<String> owners;

    public AchievementEntity() {
        level = 1;
        acquireDate = new Date();
        owners = new HashSet<>();
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public void setAcquireDate(final Date acquireDate) {
        this.acquireDate = acquireDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
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
