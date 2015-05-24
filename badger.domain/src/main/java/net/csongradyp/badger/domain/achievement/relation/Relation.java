package net.csongradyp.badger.domain.achievement.relation;

import net.csongradyp.badger.domain.IRelation;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

public class Relation implements IRelation {

    private RelationOperator operator;
    private Collection<IRelation> children;

    public Relation() {
        children = new LinkedHashSet<>();
    }

    public RelationOperator getOperator() {
        return operator;
    }

    public void setOperator(final RelationOperator operator) {
        this.operator = operator;
    }

    public void addChild(final IRelation child) {
        children.add(child);
    }

    public Collection<IRelation> getChildren() {
        return children;
    }

    @Override
    public Boolean evaluate(final Long triggerValue, final Date date, final Date time) {
        Boolean result = null;
        for (IRelation child : children) {
            final Boolean childResult = child.evaluate(triggerValue, date, time);
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
