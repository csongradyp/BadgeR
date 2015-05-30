package net.csongradyp.badger.parser.api;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.SingleAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AchievementFactoryTest {

    @Test
    public void testCreateReturnsSingleAchievementBeanWhenGivenTypeIsSingle() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.SINGLE);
        assertThat(SingleAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }

    @Test
    public void testCreateReturnsCompositeAchievementBeanWhenGivenTypeIsComposite() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.COMPOSITE);
        assertThat(CompositeAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }

    @Test
    public void testCreateReturnsScoreAchievementBeanWhenGivenTypeIsScore() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.SCORE);
        assertThat(ScoreAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }

    @Test
    public void testCreateReturnsScoreRangeAchievementBeanWhenGivenTypeIsScoreRange() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.SCORE_RANGE);
        assertThat(ScoreRangeAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }

    @Test
    public void testCreateReturnsDateAchievementBeanWhenGivenTypeIsDate() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.DATE);
        assertThat(DateAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }

    @Test
    public void testCreateReturnsTimeAchievementBeanWhenGivenTypeIsTime() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.TIME);
        assertThat(TimeAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }

    @Test
    public void testCreateReturnsTimeRangeAchievementBeanWhenGivenTypeIsTimeRange() throws Exception {
        final IAchievementBean achievementBean = AchievementFactory.create(AchievementType.TIME_RANGE);
        assertThat(TimeRangeAchievementBean.class.isAssignableFrom(achievementBean.getClass()), is(true));
    }
}