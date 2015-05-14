package net.csongradyp.badger.event;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

public interface IAchievementUnlockedEvent {

    String getId();

    String getCategory();

    String getTitle();

    String getText();

    Date getAcquireDate();

    String getTriggerValue();

    Integer getLevel();

    void setLevel(Integer level);

    AchievementEventType getEventType();

    Set<String> getOwners();

    void setOwners(Set<String> owners);

    void addOwners(Collection<String> owners);

    void addOwners(String... owners);
}
