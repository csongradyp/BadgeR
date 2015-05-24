package net.csongradyp.bdd.provider;

import java.util.Collection;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.LocalTime;

@Named
public class TriggerChecker {

    @Inject
    private DateProvider dateProvider;

    public Boolean isTriggerPresent(final ITriggerableAchievementBean achievement, final AchievementType type, final String trigger) {
        final Optional optional;
        switch (type) {
            case DATE:
                DateAchievementBean dateAchievementBean = (DateAchievementBean) achievement;
                optional = dateAchievementBean.getTrigger().stream().filter(t -> t.getDate().equals(dateProvider.parseDate(trigger))).findAny();
                break;
            case TIME:
                TimeAchievementBean timeAchievementBean = (TimeAchievementBean) achievement;
                optional = timeAchievementBean.getTrigger().stream().filter(t -> t.getTime().equals(dateProvider.parseTime(trigger))).findAny();
                break;
            case TIME_RANGE:
                optional = Optional.empty();
                break;
            case SCORE:
                final ScoreTrigger.Operation op;
                final String number = trigger.replaceAll("[+-]", "");
                final Long value = Long.valueOf(number);
                if (trigger.contains("+")) {
                    op = ScoreTrigger.Operation.GREATER_THAN;
                } else if(trigger.contains("-")) {
                    op = ScoreTrigger.Operation.LESS_THAN;
                } else {
                    op = ScoreTrigger.Operation.EQUALS;
                }
                optional = achievement.getTrigger().stream().filter(t -> ((ScoreTrigger) t).getTrigger().equals(value) && ((ScoreTrigger) t).getOperation() == op).findAny();
                break;
            case SINGLE:
            case COMPOSITE:
//                final IAchievement child = ((CompositeAchievementBean) achievement).getChildren().get(type);
//                return isTriggerPresent(child, type, trigger);
            default:
                optional = Optional.empty();
                break;
        }
        return optional.isPresent();
    }

    public Boolean isTimeRangeTriggerPresent(final Collection<? extends ITrigger> triggers, final String start, final String end) {
        final Optional optional = getTimeRangeTrigger(triggers, start, end);
        return optional.isPresent();
    }

    private Optional getTimeRangeTrigger(final Collection<? extends ITrigger> triggers, final String start, final String end) {
        return triggers.stream().filter(t -> {
            final LocalTime startTrigger = ((TimeTriggerPair) t).getStartTrigger();
            final LocalTime endTrigger = ((TimeTriggerPair) t).getEndTrigger();
            return startTrigger.equals(dateProvider.parseTime(start)) && endTrigger.equals(dateProvider.parseTime(end));
        }).findAny();
    }
}
