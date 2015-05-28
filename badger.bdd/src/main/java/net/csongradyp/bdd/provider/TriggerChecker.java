package net.csongradyp.bdd.provider;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.LocalTime;

@Named
public class TriggerChecker {

    @Inject
    private DateProvider dateProvider;

    public Boolean isTriggerPresent(final ITriggerableAchievementBean achievement, final AchievementType type, final String trigger) {
        Optional optional= Optional.empty();
        if (type == AchievementType.DATE || type == AchievementType.TIME || type == AchievementType.SCORE || type == AchievementType.COMPOSITE) {
            optional = achievement.getTrigger().stream().filter(t -> assertTrigger(trigger, (ITrigger) t)).findAny();
        }
        return optional.isPresent();
    }

    private Boolean assertTrigger(final String triggerExpression, final ITrigger trigger) {
        if (trigger.getType() == AchievementType.DATE) {
            final Date date = dateProvider.parseDate(triggerExpression);
            return ((DateTrigger) trigger).getDate().equals(date);
        } else if (trigger.getType() == AchievementType.TIME) {
            final LocalTime time = dateProvider.parseTime(triggerExpression);
            return ((TimeTrigger) trigger).getTime().equals(time);
        } else if (trigger.getType() == AchievementType.SCORE) {
            final ScoreTrigger.Operation op;
            final String number = triggerExpression.replaceAll("[+-]", "");
            final Long value = Long.valueOf(number);
            if (triggerExpression.contains("+")) {
                op = ScoreTrigger.Operation.GREATER_THAN;
            } else if (triggerExpression.contains("-")) {
                op = ScoreTrigger.Operation.LESS_THAN;
            } else {
                op = ScoreTrigger.Operation.EQUALS;
            }
            return ((ScoreTrigger) trigger).getTrigger().equals(value) && ((ScoreTrigger) trigger).getOperation() == op;
        }
        return false;
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
