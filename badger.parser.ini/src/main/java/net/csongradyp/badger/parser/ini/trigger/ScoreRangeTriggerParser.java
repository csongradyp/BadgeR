package net.csongradyp.badger.parser.ini.trigger;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.parser.api.trigger.ITriggerParser;

@Named
public class ScoreRangeTriggerParser implements ITriggerParser<ScoreTriggerPair> {

    @Override
    public List<ScoreTriggerPair> parse(final List<String> triggers) {
        final List<ScoreTriggerPair> timeRangeTriggers = new ArrayList<>();
        if (triggers.size() % 2 != 0) {
            throw new MalformedAchievementDefinition("Score range does not properly set. One of the triggers does not have an end range");
        }
        for (int i = 0; i < triggers.size() - 1; i = i + 2) {
            final Long start = Long.valueOf(triggers.get(i));
            final Long end = Long.valueOf(triggers.get(i + 1));
            timeRangeTriggers.add(new ScoreTriggerPair(start, end));
        }
        return timeRangeTriggers;
    }
}
