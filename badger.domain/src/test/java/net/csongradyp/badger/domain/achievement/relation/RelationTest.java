package net.csongradyp.badger.domain.achievement.relation;

import java.util.Collections;
import java.util.Date;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RelationTest {

    @Mock
    private ITrigger<Object> mockTrigger;
    private Relation underTest;

    @Before
    public void setUp() {
        underTest = new Relation();
    }

    @Test
    public void testEvaluateWithAndRelationReturnsTrueWhenAllUnderlyingChildEvaluationReturnsTrue() throws Exception {
        underTest.setOperator(RelationOperator.AND);
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        final Relation childRelation = new Relation();
        childRelation.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(childRelation);
        given(mockTrigger.getType()).willReturn(AchievementType.SCORE, AchievementType.DATE, AchievementType.TIME);
        given(mockTrigger.fire(any())).willReturn(true);

        final Boolean result = underTest.evaluate(1L, new Date(), new Date());

        verify(mockTrigger, times(3)).getType();
        verify(mockTrigger, times(3)).fire(any());
        assertThat(result, is(true));
    }

    @Test
    public void testEvaluateWithAndRelationReturnsFalseWhenAnyOfTheUnderlyingChildEvaluationReturnsFalse() throws Exception {
        underTest.setOperator(RelationOperator.AND);
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        final Relation childRelation = new Relation();
        childRelation.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(childRelation);
        given(mockTrigger.getType()).willReturn(AchievementType.SCORE, AchievementType.DATE, AchievementType.TIME);
        given(mockTrigger.fire(any())).willReturn(true, false, true);

        final Boolean result = underTest.evaluate(1L, new Date(), new Date());

        verify(mockTrigger, times(3)).getType();
        verify(mockTrigger, times(3)).fire(any());
        assertThat(result, is(false));
    }

    @Test
    public void testEvaluateWithOrRelationReturnsTrueWhenAnyOfTheUnderlyingChildEvaluationReturnsTrue() throws Exception {
        underTest.setOperator(RelationOperator.OR);
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        final Relation childRelation = new Relation();
        childRelation.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(childRelation);
        given(mockTrigger.getType()).willReturn(AchievementType.SCORE, AchievementType.DATE, AchievementType.TIME_RANGE);
        given(mockTrigger.fire(any())).willReturn(false, true, false);

        final Boolean result = underTest.evaluate(1L, new Date(), new Date());

        verify(mockTrigger, times(3)).getType();
        verify(mockTrigger, times(3)).fire(any());
        assertThat(result, is(true));
    }

    @Test
    public void testEvaluateWithOrRelationReturnsFalseWhenAllOfTheUnderlyingChildEvaluationReturnsTrue() throws Exception {
        underTest.setOperator(RelationOperator.OR);
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        final Relation childRelation = new Relation();
        childRelation.addChild(new RelationElement(Collections.singletonList(mockTrigger)));
        underTest.addChild(childRelation);
        given(mockTrigger.getType()).willReturn(AchievementType.SCORE, AchievementType.DATE, AchievementType.TIME_RANGE);
        given(mockTrigger.fire(any())).willReturn(false, false, false);

        final Boolean result = underTest.evaluate(1L, new Date(), new Date());

        verify(mockTrigger, times(3)).getType();
        verify(mockTrigger, times(3)).fire(any());
        assertThat(result, is(false));
    }

}
