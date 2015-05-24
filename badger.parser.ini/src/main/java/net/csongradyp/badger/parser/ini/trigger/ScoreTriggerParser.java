package net.csongradyp.badger.parser.ini.trigger;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;

@Named
public class ScoreTriggerParser implements ITriggerParser<ScoreTrigger> {

    public static final String NUMBER_TRIGGER_PATTERN = "(^\\d+$)|(^\\d+(\\+|-)$)";

    @Override
    public List<ScoreTrigger> parse(final String[] scoreTriggers) {
        final ArrayList<ScoreTrigger> triggers = new ArrayList<>();
        for (String triggerDefinition : scoreTriggers) {
            if (!triggerDefinition.matches(NUMBER_TRIGGER_PATTERN)) {
                throw new MalformedAchievementDefinition("Invalid score trigger: " + triggerDefinition);
            }
            final ScoreTrigger scoreTrigger;
            if (triggerDefinition.endsWith(ScoreTrigger.Operation.GREATER_THAN.getOperator())) {
                final long triggerValue = Long.parseLong(triggerDefinition.substring(0, triggerDefinition.length() - 1));
                scoreTrigger = new ScoreTrigger(triggerValue, ScoreTrigger.Operation.GREATER_THAN);
            } else if (triggerDefinition.endsWith(ScoreTrigger.Operation.LESS_THAN.getOperator())) {
                final long triggerValue = Long.parseLong(triggerDefinition.substring(0, triggerDefinition.length() - 1));
                scoreTrigger = new ScoreTrigger(triggerValue, ScoreTrigger.Operation.LESS_THAN);
            } else {
                final long triggerValue = Long.parseLong(triggerDefinition);
                scoreTrigger = new ScoreTrigger(triggerValue);
            }
            triggers.add(scoreTrigger);
        }
        return triggers;
    }
}
