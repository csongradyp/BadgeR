package net.csongradyp.badger.parser.ini.trigger;

import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class ScoreRangeTriggerParser implements ITriggerParser<ScoreTriggerPair> {

    @Override
    public List<ScoreTriggerPair> parse(final String[] triggers) {
        final List<ScoreTriggerPair> timeRangeTriggers = new ArrayList<>();
        if (triggers.length % 2 != 0) {
            throw new MalformedAchievementDefinition("Score range does not properly set. One of the triggers does not have an end range");
        }
        for (int i = 0; i < triggers.length - 1; i = i + 2) {
            final Long start = Long.valueOf(triggers[i]);
            final Long end = Long.valueOf(triggers[i + 1]);
            timeRangeTriggers.add(new ScoreTriggerPair(start, end));
        }
        return timeRangeTriggers;
    }
}
