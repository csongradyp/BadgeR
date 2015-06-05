package net.csongradyp.badger.event;

import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EventBusTest {

    private AchievementUnlockedHandlerWrapper unlockedHandlerWrapper;
    private ScoreUpdateHandlerWrapper scoreUpdateHandlerWrapper;
    private AchievementUnlockedEvent receivedAchievementUnlockedEvent;
    private ScoreUpdatedEvent receivedScoreUpdatedEvent;

    EventBus underTest;

    @Before
    public void setUp() {
        underTest = new EventBus();
        unlockedHandlerWrapper = new AchievementUnlockedHandlerWrapper(achievement -> receivedAchievementUnlockedEvent = achievement);
        underTest.subscribeOnUnlock(unlockedHandlerWrapper);
        scoreUpdateHandlerWrapper = new ScoreUpdateHandlerWrapper(score -> receivedScoreUpdatedEvent = score);
        underTest.subscribeOnScoreChanged(scoreUpdateHandlerWrapper);
    }

    @After
    public void tearDown() {
        underTest.unSubscribeOnUnlock(unlockedHandlerWrapper.getWrapped());
        underTest.unSubscribeOnScoreChanged(scoreUpdateHandlerWrapper.getWrapped());
        receivedAchievementUnlockedEvent = null;
    }

    @Test
    public void testOnUnlockedEventSubscriptions() throws Exception {
        final AchievementUnlockedHandlerWrapper handlerWrapper = new AchievementUnlockedHandlerWrapper(achievement -> receivedAchievementUnlockedEvent = achievement);
        underTest.subscribeOnUnlock(handlerWrapper);
        assertThat(underTest.getUnlockedSubscribers().size(), is(equalTo(2)));
        assertThat(underTest.getUnlockedSubscribers().contains(handlerWrapper), is(true));

        underTest.unSubscribeOnUnlock(handlerWrapper.getWrapped());
        assertThat(underTest.getUnlockedSubscribers().size(), is(equalTo(1)));
        assertThat(underTest.getUnlockedSubscribers().contains(handlerWrapper), is(false));
    }

    @Test
    public void testOnScoreChangedEventSubscriptions() throws Exception {
        final ScoreUpdateHandlerWrapper handlerWrapper = new ScoreUpdateHandlerWrapper(score -> receivedScoreUpdatedEvent = score);
        underTest.subscribeOnScoreChanged(handlerWrapper);
        assertThat(underTest.getScoreUpdateSubscribers().size(), is(equalTo(2)));
        assertThat(underTest.getScoreUpdateSubscribers().contains(handlerWrapper), is(true));

        underTest.unSubscribeOnScoreChanged(handlerWrapper.getWrapped());
        assertThat(underTest.getScoreUpdateSubscribers().size(), is(equalTo(1)));
        assertThat(underTest.getScoreUpdateSubscribers().contains(handlerWrapper), is(false));
    }

    @Test
    public void testPublishUnlockedPublishesGivenEvent() throws Exception {
        final String id = "test";
        final String title = "title";
        final String text = "text";
        final String score = "value";
        final int level = 3;
        final AchievementUnlockedEvent achievementUnlockedEvent = new AchievementUnlockedEvent(id, title, text, score);
        achievementUnlockedEvent.setLevel(level);

        underTest.publishUnlocked(achievementUnlockedEvent);

        assertThat(receivedAchievementUnlockedEvent, notNullValue());
        assertThat(receivedAchievementUnlockedEvent.getId(), is(equalTo(id)));
        assertThat(receivedAchievementUnlockedEvent.getTitle(), is(equalTo(title)));
        assertThat(receivedAchievementUnlockedEvent.getText(), is(equalTo(text)));
        assertThat(receivedAchievementUnlockedEvent.getTriggerValue(), is(equalTo(score)));
        assertThat(receivedAchievementUnlockedEvent.getLevel(), is(equalTo(level)));
    }

    @Test
    public void testPublishScoreChangedPublishesGivenEvent() throws Exception {
        final String event = "event";
        final long value = 123L;
        final ScoreUpdatedEvent scoreUpdatedEvent = new ScoreUpdatedEvent(event, value);

        underTest.publishScoreChanged(scoreUpdatedEvent);

        assertThat(receivedScoreUpdatedEvent, notNullValue());
        assertThat(receivedScoreUpdatedEvent.getEvent(), is(equalTo(event)));
        assertThat(receivedScoreUpdatedEvent.getValue(), is(equalTo(value)));
    }
}
