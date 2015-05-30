package net.csongradyp.badger.parser.api.trigger;

import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.provider.date.DateProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class DateTriggerParser implements ITriggerParser<DateTrigger> {

    private final DateProvider dateProvider;

    @Inject
    public DateTriggerParser(final DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    @Override
    public List<DateTrigger> parse(final List<String> triggers) {
        final List<DateTrigger> timeTriggers = new ArrayList<>();
        for (String trigger : triggers) {
            final Date date = dateProvider.parseDate(trigger);
            final DateTrigger timeTrigger = new DateTrigger(date);
            timeTriggers.add(timeTrigger);
        }
        return timeTriggers;
    }

}
