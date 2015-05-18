package net.csongradyp.badger.parser.ini;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.IRelation;
import net.csongradyp.badger.domain.achievement.CounterAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.relation.ChildAchievement;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.domain.achievement.relation.RelationOperator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RelationParserTest {

    public static final String ID = "test";
    @Mock
    private RelationValidator mockRelationValidator;
    @Mock
    private IAchievementUnlockFinderFacade mockUnlockFinderFacade;
    @Mock
    private IAchievementBean mockAchievementBean;

    private RelationParser underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new RelationParser();
        underTest.setRelationValidator(mockRelationValidator);

        when(mockUnlockFinderFacade.getUnlockable(any(IAchievementBean.class))).thenReturn(Optional.empty());
    }

    @Test
    public void testParseReturnsOneLevelRelationWhenNoBracketsArePresentInTheRelationDefinition() {
        final String definition = "date & time";
        final Collection<IAchievement> achievements = new ArrayList<>();
        final DateAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ID);
        achievements.add(dateAchievementBean);
        final TimeAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ID);
        achievements.add(timeAchievementBean);

        final Relation relation = underTest.parse(ID, definition, achievements);

        assertThat(relation.getOperator(), equalTo(RelationOperator.AND));
        assertThat(relation.getChildren().size(), equalTo(2));
        for (IRelation iRelation : relation.getChildren()) {
            assertThat(iRelation, instanceOf(ChildAchievement.class));
        }
    }

    @Test
    public void testParseReturnsTwoLevelRelationWhenOnePairOfBracketsArePresentInTheRelationDefinition() {
        final String definition = "(date & time) | counter";
        final Collection<IAchievement> achievements = new ArrayList<>();
        final DateAchievementBean dateAchievementBean = new DateAchievementBean();
        dateAchievementBean.setId(ID);
        achievements.add(dateAchievementBean);
        final TimeAchievementBean timeAchievementBean = new TimeAchievementBean();
        timeAchievementBean.setId(ID);
        achievements.add(timeAchievementBean);
        final CounterAchievementBean counterAchievementBean = new CounterAchievementBean();
        counterAchievementBean.setId(ID);
        achievements.add(counterAchievementBean);

        final Relation relation = underTest.parse(ID, definition, achievements);

        assertThat(relation.getOperator(), equalTo(RelationOperator.OR));
        assertThat(relation.getChildren().size(), equalTo(2));
        assertThat(relation.getChildren().iterator().next(), instanceOf(Relation.class));
        assertThat(((Relation)relation.getChildren().iterator().next()).getChildren().size(), equalTo(2));
        for (IRelation iRelation : ((Relation)relation.getChildren().iterator().next()).getChildren()) {
            assertThat(iRelation, instanceOf(ChildAchievement.class));
        }
    }
}
