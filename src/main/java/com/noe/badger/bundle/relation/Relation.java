package com.noe.badger.bundle.relation;

import com.noe.badger.AchievementController;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Relation implements IRelation {

    private RelationOperator operator;
    private Set<IRelation> children;

    public Relation() {
        children = new HashSet<>();
    }

    public Relation(final RelationOperator operator) {
        this();
        this.operator = operator;
    }

    public Relation(final RelationOperator operator, final Set<IRelation> children) {
        this.operator = operator;
        this.children = children;
    }

    public Relation(final RelationOperator operator, final Relation... children) {
        this.operator = operator;
        Collections.addAll(this.children, children);
    }

    public RelationOperator getOperator() {
        return operator;
    }

    public void setOperator(final String operator) {
        this.operator = RelationOperator.parse(operator);
    }

    public void setOperator(final RelationOperator operator) {
        this.operator = operator;
    }

    public void addChild(final IRelation child) {
        children.add(child);
    }

    @Override
    public Boolean evaluate(final AchievementController controller) {
        Boolean result = null;
        for (IRelation child : children) {
            final Boolean childResult = child.evaluate(controller);
            if (result == null) {
                result = childResult;
            } else if (operator == RelationOperator.AND) {
                result &= childResult;
            } else {
                result |= childResult;
            }
        }
        return result;
    }

}
