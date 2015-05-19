package net.csongradyp.bdd.provider;

import java.util.Date;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.NumberTrigger;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.provider.date.DateProvider;

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
                optional = Optional.empty();
                break;
            case COUNTER:
                final NumberTrigger.Operation op;
                final String number = trigger.replaceAll("[+-]", "");
                final Long value = Long.valueOf(number);
                if (trigger.contains("+")) {
                    op = NumberTrigger.Operation.GREATER_THAN;
                } else if(trigger.contains("-")) {
                    op = NumberTrigger.Operation.LESS_THAN;
                } else {
                    op = NumberTrigger.Operation.EQUALS;
                }
                optional = achievement.getTrigger().stream().filter(t -> ((NumberTrigger) t).getTrigger().equals(value) && ((NumberTrigger) t).getOperation() == op).findAny();
                break;
            case SINGLE:
            case COMPOSITE:
                final IAchievement child = ((CompositeAchievementBean) achievement).getChildren().get(type);
                return isTriggerPresent(child, type, trigger);
            default:
                optional = Optional.empty();
                break;
        }
        return optional.isPresent();
    }

    public Boolean isTimeRangeTriggerPresent(final IAchievement achievement, final String start, final String end) {
        final Optional optional = getTimeRangeTrigger(achievement, start, end);
        return optional.isPresent();
    }

    private Optional getTimeRangeTrigger(final IAchievement achievement, final String start, final String end) {
        return achievement.getTrigger().stream().filter(t -> {
            final Date startTrigger = ((TimeTriggerPair) t).getStartTrigger();
            final Date endTrigger = ((TimeTriggerPair) t).getEndTrigger();
            return start.equals(dateProvider.getTime(startTrigger)) && end.equals(dateProvider.getTime(endTrigger));
        }).findAny();
    }
}
