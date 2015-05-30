package net.csongradyp.badger.parser.api;

import net.csongradyp.badger.exception.MalformedAchievementRelationDefinition;
import net.csongradyp.badger.parser.api.RelationValidator;
import org.junit.Before;
import org.junit.Test;

public class RelationValidatorTest {

    private RelationValidator underTest;

    @Before
    public void setUp() {
        underTest = new RelationValidator();
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowExceptionWhenRelationContainsInvalidOperators() throws Exception {
        final String missingBracketRelation = "score + (date * time)";
        underTest.validate(missingBracketRelation);
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowExceptionWhenRelationInputStartsWithOperator() throws Exception {
        final String missingBracketRelation = "& score & (date | time)";
        underTest.validate(missingBracketRelation);
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowExceptionWhenRelationInputEndsWithOperator() throws Exception {
        final String missingBracketRelation = "score & (date | time) &";
        underTest.validate(missingBracketRelation);
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowExceptionWhenThereAreAnyMissingCloseBrackets() throws Exception {
        final String missingBracketRelation = "score & (date | time";
        underTest.validate(missingBracketRelation);
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowExceptionWhenThereAreAnyMissingOpenBrackets() throws Exception {
        final String missingBracketRelation = "score & date | time)";
        underTest.validate(missingBracketRelation);
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowExceptionWhenRelationContainsInvalidIdentifiers() throws Exception {
        final String missingBracketRelation = "score & ( notValidKeyword & time )";
        underTest.validate(missingBracketRelation);
    }

}
