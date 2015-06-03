package net.csongradyp.badger.domain.achievement;

import java.util.Arrays;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompositeAchievementBeanTest {

    private CompositeAchievementBean underTest;

    @Before
    public void setUp() {
        underTest = new CompositeAchievementBean();
    }

    @Test
    public void testAddTrigger() {
        final ScoreTrigger addedTrigger = new ScoreTrigger(1L);

        underTest.addTrigger(Arrays.asList(addedTrigger));

        assertThat(underTest.getTrigger().size(), is(equalTo(1)));
        assertThat(underTest.getTrigger().get(0), is(addedTrigger));
    }

    @Test
    public void testGetTypeReturnsCompositeType() throws Exception {
        assertThat(underTest.getType(), is(AchievementType.COMPOSITE));
    }
}