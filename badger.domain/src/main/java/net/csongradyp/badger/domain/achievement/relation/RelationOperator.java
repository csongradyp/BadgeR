package net.csongradyp.badger.domain.achievement.relation;

public enum RelationOperator {

    AND("&"), OR("|");

    private final String operator;

    RelationOperator(final String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

}
