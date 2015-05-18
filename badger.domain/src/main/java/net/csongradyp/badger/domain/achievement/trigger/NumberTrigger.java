package net.csongradyp.badger.domain.achievement.trigger;

public class NumberTrigger {

    public enum Operation {
        EQUALS, GREATER_THAN, LESS_THAN;
    }

    private final Long trigger;
    private final Operation operation;

    public NumberTrigger(final Long trigger) {
        this.trigger = trigger;
        operation = Operation.EQUALS;
    }

    public NumberTrigger(final Long trigger, final Operation operation) {
        this.trigger = trigger;
        this.operation = operation;
    }

    public Long getTrigger() {
        return trigger;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return operation + " " + trigger;
    }
}
