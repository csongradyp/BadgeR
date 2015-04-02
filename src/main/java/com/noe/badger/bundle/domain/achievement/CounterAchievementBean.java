package com.noe.badger.bundle.domain.achievement;

import com.noe.badger.bundle.domain.AbstractAchievementBean;
import com.noe.badger.bundle.trigger.NumberTrigger;
import com.noe.badger.exception.MalformedAchievementDefinition;
import java.util.ArrayList;
import java.util.List;

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
        validateTriggers();
        setMaxLevel(this.trigger.size());
    }

    private void validateTriggers() {
        for (int i = 0; i < this.trigger.size() - 1; i++) {
            NumberTrigger first = this.trigger.get(i);
            NumberTrigger second = this.trigger.get(i + 1);
            if (first.getTrigger() > second.getTrigger()) {
                throw new MalformedAchievementDefinition("Triggers are not properly set for achievement: " + getId() + ". Nr. " + i + ". trigger should be less than the next one");
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "trigger=" + trigger +
                '}';
    }
}
