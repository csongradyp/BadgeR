package net.csongradyp.badger.parser.json.domain;

import java.util.List;

public interface IRangeTriggerAchievementJson<T extends RangeTrigger> extends IAchievementJson {

    List<T> getTrigger();

}
