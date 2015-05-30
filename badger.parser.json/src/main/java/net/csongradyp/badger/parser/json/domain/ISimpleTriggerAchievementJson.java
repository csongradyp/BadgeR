package net.csongradyp.badger.parser.json.domain;

import java.util.List;

public interface ISimpleTriggerAchievementJson<T> extends IAchievementJson {

    List<T> getTrigger();

}
