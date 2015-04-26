package com.noe.badger.bundle.relation;

import com.noe.badger.AchievementController;
import com.noe.badger.AchievementType;
import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievementBean;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationParserTest {

    @Mock
    private RelationValidator mockRelationValidator;
    @Mock
    private AchievementBundle mockAchievementBundle;
    @Mock
    private AchievementController mockController;
    @Mock
    private IAchievementBean mockAchievementBean;

    private RelationParser underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new RelationParser();
        underTest.setAchievementBundle(mockAchievementBundle);
        underTest.setRelationValidator(mockRelationValidator);

        when(mockAchievementBundle.get(any(AchievementType.class), anyString())).thenReturn(mockAchievementBean);
        when(mockController.unlockable(anyLong(), any(IAchievementBean.class))).thenReturn(Optional.empty());
    }

    @Test
    public void testParseReturnsOneLevelRelationWhenNoBracketsArePresentInTheRelationDefinition() {
        final String definition = "date & time";

        final Relation relation = underTest.parse("test", definition);

        assertThat(relation.getOperator(), equalTo(RelationOperator.AND));
        assertThat(relation.getChildren().size(), equalTo(2));
        for (IRelation iRelation : relation.getChildren()) {
            assertThat(iRelation, instanceOf(RelatedAchievement.class));
        }
        assertThat(relation.evaluate(mockController), is(true));
    }

    @Test
    public void testParseReturnsTwoLevelRelationWhenOnePairOfBracketsArePresentInTheRelationDefinition() {
        final String definition = "(date & time) | counter";

        final Relation relation = underTest.parse("test", definition);

        assertThat(relation.getOperator(), equalTo(RelationOperator.OR));
        assertThat(relation.getChildren().size(), equalTo(2));
        assertThat(relation.getChildren().iterator().next(), instanceOf(Relation.class));
        assertThat(((Relation)relation.getChildren().iterator().next()).getChildren().size(), equalTo(2));
        for (IRelation iRelation : ((Relation)relation.getChildren().iterator().next()).getChildren()) {
            assertThat(iRelation, instanceOf(RelatedAchievement.class));
        }
        assertThat(relation.evaluate(mockController), is(true));
    }
}
