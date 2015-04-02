package com.noe.badger.bundle.relation;

public enum RelationOperator {

    AND("&"), OR("|");

    private final String operator;

    RelationOperator(final String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public static RelationOperator parse(final String operator) {
        for ( RelationOperator registeredOperator : values() ) {
            if(registeredOperator.getOperator().equals(operator)) {
                return registeredOperator;
            }
        }
        throw new RuntimeException("not a valid operator: " + operator + " valid operators are '&' and '|'");
    }
}
