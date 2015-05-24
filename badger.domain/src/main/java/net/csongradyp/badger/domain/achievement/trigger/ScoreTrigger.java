package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;

public class ScoreTrigger implements ITrigger<Long> {

    public enum Operation {
        EQUALS(""), GREATER_THAN("+"), LESS_THAN("-");

        private final String operator;

        Operation(final String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }

    private final Long trigger;
    private final Operation operation;

    public ScoreTrigger(final Long trigger) {
        this.trigger = trigger;
        operation = Operation.EQUALS;
    }

    public ScoreTrigger(final Long trigger, final Operation operation) {
        this.trigger = trigger;
        this.operation = operation;
    }

    @Override
    public Boolean fire(final Long triggerValue) {
        Boolean triggered = false;
        switch (operation) {
            case GREATER_THAN:
                triggered = triggerValue >= trigger;
                break;
            case LESS_THAN:
                triggered = triggerValue <= trigger;
                break;
            case EQUALS:
                triggered = triggerValue.equals(trigger);
                break;
        }
        return triggered;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.SCORE;
    }

    public Long getTrigger() {
        return trigger;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return trigger + operation.getOperator();
    }
}
