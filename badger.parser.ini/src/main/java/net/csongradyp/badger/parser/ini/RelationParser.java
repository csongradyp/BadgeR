package net.csongradyp.badger.parser.ini;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IRelation;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.domain.achievement.relation.RelationElement;
import net.csongradyp.badger.domain.achievement.relation.RelationOperator;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.exception.MalformedAchievementRelationDefinition;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class RelationParser {

    @Inject
    private RelationValidator relationValidator;

    public Relation parse(final String relationExpression, final Collection<ITrigger> triggers) {
        if(relationExpression == null) {
            throw new MalformedAchievementRelationDefinition("Relation expression should be present!");
        }
        String normalizedRelation = relationExpression.toLowerCase().replaceAll("\\s", "");
        relationValidator.validate(normalizedRelation);
        final Stack<Relation> relationStack = new Stack<>();
        relationStack.push(new Relation());
        while (!normalizedRelation.isEmpty()) {
            if (normalizedRelation.startsWith( RelationOperator.AND.getOperator())) {
                final Relation currentRelation = relationStack.peek();
                setOperator(currentRelation, RelationOperator.AND);
                normalizedRelation = normalizedRelation.substring(1);
            } else if (normalizedRelation.startsWith(RelationOperator.OR.getOperator())) {
                final Relation currentRelation = relationStack.peek();
                setOperator(currentRelation, RelationOperator.OR);
                normalizedRelation = normalizedRelation.substring(1);
            } else if (normalizedRelation.startsWith("(")) {
                relationStack.push(new Relation());
                normalizedRelation = normalizedRelation.substring(1);
            } else if (normalizedRelation.startsWith(")")) {
                final IRelation relationGroup = relationStack.pop();
                relationStack.peek().addChild(relationGroup);
                normalizedRelation = normalizedRelation.substring(1);
            } else {
                Integer nextIndex = getNextElementStartIndex(normalizedRelation);
                final String achievementTypeString = normalizedRelation.substring(0, nextIndex);
                final AchievementType achievementType = AchievementType.parse(achievementTypeString);
                final Collection<ITrigger> typeTriggers = triggers.stream().filter(t -> t.getType() == achievementType).collect(Collectors.toList());
                if (typeTriggers != null && !typeTriggers.isEmpty()) {
                    relationStack.peek().addChild(new RelationElement(typeTriggers));
                }
                normalizedRelation = normalizedRelation.substring(nextIndex);
            }
        }
        return relationStack.pop();
    }

    private void setOperator(final Relation currentRelation, final RelationOperator operator) {
        if(currentRelation.getOperator() != null && currentRelation.getOperator() != operator) {
            throw new MalformedAchievementRelationDefinition("Not a valid relation sequence");
        }
        currentRelation.setOperator(operator);
    }

    private Integer getNextElementStartIndex( String normalizedRelation ) {
        final int and = normalizedRelation.contains( RelationOperator.AND.getOperator()) ? normalizedRelation.indexOf(RelationOperator.AND.getOperator()) : normalizedRelation.length();
        final int or = normalizedRelation.contains(RelationOperator.OR.getOperator()) ? normalizedRelation.indexOf(RelationOperator.OR.getOperator()) : normalizedRelation.length();
        final int open = normalizedRelation.contains("(") ? normalizedRelation.indexOf("(") : normalizedRelation.length();
        final int close = normalizedRelation.contains(")") ? normalizedRelation.indexOf(")") : normalizedRelation.length();
        return Collections.min( Arrays.asList( and, or, open, close ) );
    }

    void setRelationValidator(final RelationValidator relationValidator) {
        this.relationValidator = relationValidator;
    }

}
