package com.noe.badger.bundle.domain;

import com.noe.badger.AchievementController;
import com.noe.badger.AchievementType;
import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.relation.RelatedAchievement;
import com.noe.badger.bundle.relation.IRelation;
import com.noe.badger.bundle.relation.Relation;
import com.noe.badger.bundle.relation.RelationOperator;
import com.noe.badger.exception.MalformedAchievementRelationDefinition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CompositeAchievementBean implements IAchievementBean<Boolean> {

    private static final String REGEX = "(\\(*(\\w+\\s*[&|]?\\s*)*[^&|]\\)*)*";
    private Relation relation;
    private final String id;
    private Map<AchievementType, IAchievementBean> children;

    public CompositeAchievementBean( final String id, final String relation, final AchievementBundle achievementBundle) {
        this.id = id;
        children = new HashMap<>();
        String normalizedRelation = relation.toLowerCase().replaceAll("\\s", "");
        validateSyntactic(normalizedRelation);
        this.relation = parse(normalizedRelation, achievementBundle);
    }

    private Relation parse(final String relation, final  AchievementBundle achievementBundle) {
        String normalizedRelation = relation;
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
                final IAchievementBean achievementBean = achievementBundle.parse(achievementType, id);
                if(!children.containsKey(achievementType)) {
                    children.put(achievementType, achievementBean);
                }
                relationStack.peek().addChild( new RelatedAchievement(achievementBean) );
                normalizedRelation = normalizedRelation.substring(nextIndex);
            }
        }
        return relationStack.pop();
    }

    private void validateSyntactic(final String normalizedRelation) {
        if (!normalizedRelation.matches(REGEX)) {
            throw new MalformedAchievementRelationDefinition("Relation contains unwanted characters.");
        }

        final int openBracketNumber = StringUtils.countOccurrencesOf(normalizedRelation, "(");
        final int closeBracketNumber = StringUtils.countOccurrencesOf(normalizedRelation, ")");
        if (openBracketNumber > closeBracketNumber) {
            throw new MalformedAchievementRelationDefinition("Missing close bracket");
        } else if (openBracketNumber < closeBracketNumber) {
            throw new MalformedAchievementRelationDefinition("Missing open bracket");
        }
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

    public Map<AchievementType, IAchievementBean> getChildren() {
        return children;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {

    }

    @Override
    public String getCategory() {
        return "composite";
    }

    @Override
    public void setCategory(final String category) {

    }

    @Override
    public List<String> getEvent() {
        List<String> events = new ArrayList<>();
        children.values().forEach( achievement -> events.addAll( achievement.getEvent() ) );
        return events;
    }

    @Override
    public void setEvent( String[] event ) {

    }

    @Override
    public String getTitleKey() {
        return children.values().iterator().next().getTitleKey();
    }

    @Override
    public String getTextKey() {
        return children.values().iterator().next().getTextKey();
    }

    @Override
    public List<Boolean> getTrigger() {
        return null;
    }

    @Override
    public void setTrigger( String[] trigger ) {

    }

    @Override
    public Integer getMaxLevel() {
        return -1;
    }

    @Override
    public void setMaxLevel( Integer maxLevels ) {

    }

    public Boolean evaluate(AchievementController controller) {
        return relation.evaluate(controller);
    }

}
