package net.csongradyp.badger.domain;

import java.util.List;

public interface ITriggerable<TRIGGER_TYPE> {

    void setTrigger(List<TRIGGER_TYPE> trigger);

    List<TRIGGER_TYPE> getTrigger();
}
