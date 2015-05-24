package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Date;

public class DateTrigger implements ITrigger<Date> {

    private final Date date;

    public DateTrigger(final Date date) {
        this.date = date;
    }

    @Override
    public Boolean fire(final Date triggerValue) {
        final LocalDate nowLocal = new DateTime(date).withYear(2000).toLocalDate();
        final LocalDate triggerLocal = new DateTime(triggerValue).withYear(2000).toLocalDate();
        return nowLocal.isEqual(triggerLocal);
    }

    @Override
    public AchievementType getType() {
        return AchievementType.DATE;
    }

    public Date getDate() {
        return date;
    }
}
