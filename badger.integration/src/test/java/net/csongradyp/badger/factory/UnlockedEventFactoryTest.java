package net.csongradyp.badger.factory;

import java.util.Locale;
import java.util.ResourceBundle;
import net.csongradyp.badger.domain.achievement.CounterAchievementBean;
import net.csongradyp.badger.event.AchievementEventType;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UnlockedEventFactoryTest {

    private UnlockedEventFactory underTest;

    @Before
    public void setUp() {
        underTest = new UnlockedEventFactory();
        underTest.setResourceBundle(null);
    }

    @Test
    public void testCreateEventWithLevelAndTrigger() {
        final String id = "TestId";
        final String category = "some category";
        final int level = 5;
        final CounterAchievementBean achievementBean = givenAchievementBean(id, category);

        final AchievementUnlockedEvent event = underTest.createEvent(achievementBean, level, 456L);

        assertThat(event, notNullValue());
        assertThat(event.getId(), is(id));
        assertThat(event.getTriggerValue(), is("456"));
        assertThat(event.getCategory(), is(category));
        assertThat(event.getLevel(), is(level));
        assertThat(event.getEventType(), is(AchievementEventType.UNLOCK));
        assertThat(event.getTitle(), is("TestId.title"));
        assertThat(event.getText(), is("TestId.text"));
    }

    @Test
    public void testCreateEventWithTriggerAndOwners() {
        final String id = "TestId";
        final String category = "some category";
        final String triggeredValue = "456";
        final String[] owners = {"foo", "bar"};
        final CounterAchievementBean achievementBean = givenAchievementBean(id, category);

        final AchievementUnlockedEvent event = underTest.createEvent(achievementBean, triggeredValue, owners);

        assertThat(event, notNullValue());
        assertThat(event.getId(), is(id));
        assertThat(event.getTriggerValue(), is(triggeredValue));
        assertThat(event.getCategory(), is(category));
        assertThat(event.getLevel(), is(equalTo(1)));
        assertThat(event.getOwners().size(), is(equalTo(2)));
        assertThat(event.getOwners(), hasItems(owners));
        assertThat(event.getEventType(), is(AchievementEventType.UNLOCK));
        assertThat(event.getTitle(), is("TestId.title"));
        assertThat(event.getText(), is("TestId.text"));
    }

    @Test
    public void testCreateEventWithoutTriggerInformation() {
        final String id = "TestId";
        final String category = "some category";
        final CounterAchievementBean achievementBean = givenAchievementBean(id, category);

        final AchievementUnlockedEvent event = underTest.createEvent(achievementBean);

        assertThat(event, notNullValue());
        assertThat(event.getId(), is(id));
        assertThat(event.getCategory(), is(category));
        assertThat(event.getTriggerValue(), is(""));
        assertThat(event.getLevel(), is(equalTo(1)));
        assertThat(event.getOwners().isEmpty(), is(true));
        assertThat(event.getEventType(), is(AchievementEventType.UNLOCK));
        assertThat(event.getTitle(), is("TestId.title"));
        assertThat(event.getText(), is("TestId.text"));
    }

    @Test
    public void testCreateEventWithLocalizedMessages() {
        underTest.setResourceBundle(ResourceBundle.getBundle("msg", Locale.ENGLISH));
        final String id = "simple";
        final String category = "some category";
        final String localizedTitle = "Simple";
        final String localizedText = "simple text";
        final CounterAchievementBean achievementBean = givenAchievementBean(id, category);

        final AchievementUnlockedEvent event = underTest.createEvent(achievementBean);

        assertThat(event, notNullValue());
        assertThat(event.getId(), is(id));
        assertThat(event.getCategory(), is(category));
        assertThat(event.getTriggerValue(), is(""));
        assertThat(event.getLevel(), is(equalTo(1)));
        assertThat(event.getOwners().isEmpty(), is(true));
        assertThat(event.getEventType(), is(AchievementEventType.UNLOCK));
        assertThat(event.getTitle(), is(localizedTitle));
        assertThat(event.getText(), is(localizedText));
    }

    private CounterAchievementBean givenAchievementBean(String id, String category) {
        final CounterAchievementBean achievementBean = new CounterAchievementBean();
        achievementBean.setId(id);
        achievementBean.setCategory(category);
        return achievementBean;
    }
}