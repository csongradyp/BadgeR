package net.csongradyp.badger.parser.ini.trigger;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.LocalTime;

@Named
public class TimeRangeTriggerParser implements ITriggerParser<TimeTriggerPair> {

    private final DateProvider dateProvider;

    @Inject
    public TimeRangeTriggerParser(final DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    @Override
    public List<TimeTriggerPair> parse(final String[] triggers) {
        final List<TimeTriggerPair> timeRangeTriggers = new ArrayList<>();
        if (triggers.length % 2 != 0) {
            throw new MalformedAchievementDefinition("Time range does not properly set. One of the triggers does not have an end time");
        }
        for (int i = 0; i < triggers.length - 1; i = i + 2) {
            final LocalTime start = dateProvider.parseTime(triggers[i]);
            final LocalTime end = dateProvider.parseTime(triggers[i + 1]);
            final TimeTriggerPair timeTriggerPair = new TimeTriggerPair(start, end);
            timeRangeTriggers.add(timeTriggerPair);
        }
        return timeRangeTriggers;
    }
}
