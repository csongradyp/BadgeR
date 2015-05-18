package net.csongradyp.bdd.provider;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.NumberTrigger;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Optional;

@Named
public class TriggerChecker {

    @Inject
    private DateProvider dateProvider;

    public Boolean isTriggerPresent(final IAchievement achievement, final AchievementType type, final String trigger) {
        final Optional optional;
        switch (type) {
            case DATE:
            case TIME:
                optional = achievement.getTrigger().stream().filter(t -> t.equals(trigger)).findAny();
                break;
            case TIME_RANGE:
                optional = achievement.getTrigger().stream().filter(t -> {
                    final Date startTrigger = ((TimeRangeAchievementBean.TimeTriggerPair) t).getStartTrigger();
                    final Date endTrigger = ((TimeRangeAchievementBean.TimeTriggerPair) t).getEndTrigger();
                    return startTrigger.equals(Long.valueOf(trigger));
                }).findAny();
                break;
            case COUNTER:
                optional = achievement.getTrigger().stream().filter(t -> ((NumberTrigger) t).getTrigger().equals(Long.valueOf(trigger))).findAny();
                break;
            case SINGLE:
            case COMPOSITE:
            default:
                optional = Optional.empty();
                break;
        }
        return optional.isPresent();
    }

    public Boolean isTimeRangeTriggerPresent(final IAchievement achievement, final String start, final String end) {
        final Optional optional = achievement.getTrigger().stream().filter(t -> {
            final Date startTrigger = ((TimeRangeAchievementBean.TimeTriggerPair) t).getStartTrigger();
            final Date endTrigger = ((TimeRangeAchievementBean.TimeTriggerPair) t).getEndTrigger();
            return start.equals(dateProvider.getTime(startTrigger)) && end.equals(dateProvider.getTime(endTrigger));
       }).findAny();
        return optional.isPresent();
    }
}
