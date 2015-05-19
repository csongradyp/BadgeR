package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.NumberTrigger;

public class CounterAchievementBean extends AbstractAchievementBean<NumberTrigger> {

    public static final String NUMBER_TRIGGER_PATTERN = "(^\\d+$)|(^\\d+(\\+|-)$)";
    private List<NumberTrigger> trigger;

    public CounterAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<NumberTrigger> getTrigger() {
        return trigger;
    }

    public void setTrigger(final String[] trigger) {
        this.trigger = new ArrayList<>(trigger.length);
        for (String triggerDefinition : trigger) {
            if (triggerDefinition.matches(NUMBER_TRIGGER_PATTERN)) {
                final NumberTrigger numberTrigger;
                if (triggerDefinition.endsWith("+")) {
                    final long triggerValue = Long.parseLong(triggerDefinition.substring(0, triggerDefinition.length() - 1));
                    numberTrigger = new NumberTrigger(triggerValue, NumberTrigger.Operation.GREATER_THAN);
                } else if (triggerDefinition.endsWith("-")) {
                    final long triggerValue = Long.parseLong(triggerDefinition.substring(0, triggerDefinition.length() - 1));
                    numberTrigger = new NumberTrigger(triggerValue, NumberTrigger.Operation.LESS_THAN);
                } else {
                    final long triggerValue = Long.parseLong(triggerDefinition);
                    numberTrigger = new NumberTrigger(triggerValue);
                }
                this.trigger.add(numberTrigger);
            }
        }
        setMaxLevel(this.trigger.size());
    }

    @Override
    public String toString() {
        return super.toString() +
                "trigger=" + trigger +
                '}';
    }

    @Override
    public AchievementType getType() {
        return AchievementType.COUNTER;
    }
}
