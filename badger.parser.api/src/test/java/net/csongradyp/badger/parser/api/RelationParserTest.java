package net.csongradyp.badger.parser.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import net.csongradyp.badger.domain.IRelation;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.domain.achievement.relation.RelationElement;
import net.csongradyp.badger.domain.achievement.relation.RelationOperator;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.exception.MalformedAchievementRelationDefinition;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class RelationParserTest {

    public static final String ID = "test";
    @Mock
    private RelationValidator mockRelationValidator;
    @Mock
    private RelationParser underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new RelationParser();
        underTest.setRelationValidator(mockRelationValidator);
    }

    @Test
    public void testParseReturnsOneLevelRelationWhenNoBracketsArePresentInTheRelationDefinition() {
        final String definition = "date & time";
        final Collection<ITrigger> triggers = new ArrayList<>();
        triggers.add(new DateTrigger(new Date()));
        triggers.add(new TimeTrigger(new LocalTime(11, 11)));

        final Relation relation = underTest.parse(definition, triggers);

        assertThat(relation.getOperator(), equalTo(RelationOperator.AND));
        assertThat(relation.getChildren().size(), equalTo(2));
        for (IRelation iRelation : relation.getChildren()) {
            assertThat(iRelation, instanceOf(RelationElement.class));
        }
    }

    @Test
    public void testParseReturnsTwoLevelRelationWhenOnePairOfBracketsArePresentInTheRelationDefinition() {
        final String definition = "(date & time) | score";
        final Collection<ITrigger> triggers = new ArrayList<>();
        triggers.add(new DateTrigger(new Date()));
        triggers.add(new TimeTrigger(new LocalTime(1, 1)));
        triggers.add(new ScoreTrigger(1L));

        final Relation relation = underTest.parse(definition, triggers);

        assertThat(relation.getOperator(), equalTo(RelationOperator.OR));
        assertThat(relation.getChildren().size(), equalTo(2));
        assertThat(relation.getChildren().iterator().next(), instanceOf(Relation.class));
        assertThat(((Relation) relation.getChildren().iterator().next()).getChildren().size(), equalTo(2));
        for (IRelation iRelation : ((Relation) relation.getChildren().iterator().next()).getChildren()) {
            assertThat(iRelation, instanceOf(RelationElement.class));
        }
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowsExceptionWhenGivenExpressionIsNull() {
        underTest.parse(null, new ArrayList<>());
    }

    @Test(expected = MalformedAchievementRelationDefinition.class)
    public void testParseThrowsExceptionWhenGivenExpressionValidationFails() {
        doThrow(new MalformedAchievementRelationDefinition("")).when(mockRelationValidator).validate(anyString());
        underTest.parse("something wrong", new ArrayList<>());
    }
}
