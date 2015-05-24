package net.csongradyp.badger.parser.ini.trigger;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.LocalTime;

@Named
public class TimeTriggerParser implements ITriggerParser<TimeTrigger> {

    private final DateProvider dateProvider;

    @Inject
    public TimeTriggerParser(final DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    @Override
    public List<TimeTrigger> parse(final String[] triggers) {
        final List<TimeTrigger> timeTriggers = new ArrayList<>();
        for (String trigger : triggers) {
            final LocalTime date = dateProvider.parseTime(trigger);
            final TimeTrigger timeTrigger = new TimeTrigger(date);
            timeTriggers.add(timeTrigger);
        }
        return timeTriggers;
    }
}
