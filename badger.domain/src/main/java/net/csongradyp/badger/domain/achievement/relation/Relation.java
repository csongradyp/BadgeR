package net.csongradyp.badger.domain.achievement.relation;

import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.IRelation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Relation implements IRelation {

    private RelationOperator operator;
    private Collection<IRelation> children;

    public Relation() {
        children = new LinkedHashSet<>();
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
    public Boolean evaluate(final IAchievementUnlockFinderFacade controller) {
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
