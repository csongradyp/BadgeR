package net.csongradyp.badger.event;

import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.event.message.Score;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventBusTest {

    @Mock
    private IAchievementController mockController;
    private AchievementUnlockedHandlerWrapper unlockedHandlerWrapper;
    private ScoreUpdateHandlerWrapper scoreUpdateHandlerWrapper;
    private AchievementUnlockedEvent receivedAchievementUnlockedEvent;
    private Score receivedScore;

    @Before
    public void setUp()  {
        unlockedHandlerWrapper = new AchievementUnlockedHandlerWrapper(achievement -> receivedAchievementUnlockedEvent = achievement);
        EventBus.subscribeOnUnlock(unlockedHandlerWrapper);
        scoreUpdateHandlerWrapper = new ScoreUpdateHandlerWrapper(score -> receivedScore = score);
        EventBus.subscribeOnScoreChanged(scoreUpdateHandlerWrapper);
        EventBus.setController(mockController);
    }

    @After
    public void tearDown()  {
        EventBus.unSubscribeOnUnlock(unlockedHandlerWrapper.getWrapped());
        EventBus.unSubscribeOnScoreChanged(scoreUpdateHandlerWrapper.getWrapped());
        receivedAchievementUnlockedEvent = null;
    }

    @Test
    public void testOnUnlockedEventSubscriptions() throws Exception {
        final  AchievementUnlockedHandlerWrapper handlerWrapper = new AchievementUnlockedHandlerWrapper(achievement -> receivedAchievementUnlockedEvent = achievement);
        EventBus.subscribeOnUnlock(handlerWrapper);
        assertThat(EventBus.getUnlockedSubscribers().size(), is(equalTo(2)));
        assertThat(EventBus.getUnlockedSubscribers().contains(handlerWrapper), is(true));

        EventBus.unSubscribeOnUnlock(handlerWrapper.getWrapped());
        assertThat(EventBus.getUnlockedSubscribers().size(), is(equalTo(1)));
        assertThat(EventBus.getUnlockedSubscribers().contains(handlerWrapper), is(false));
    }

    @Test
    public void testOnScoreChangedEventSubscriptions() throws Exception {
        final ScoreUpdateHandlerWrapper handlerWrapper = new ScoreUpdateHandlerWrapper(score -> receivedScore = score);
        EventBus.subscribeOnScoreChanged(handlerWrapper);
        assertThat(EventBus.getScoreUpdateSubscribers().size(), is(equalTo(2)));
        assertThat(EventBus.getScoreUpdateSubscribers().contains(handlerWrapper), is(true));

        EventBus.unSubscribeOnScoreChanged(handlerWrapper.getWrapped());
        assertThat(EventBus.getScoreUpdateSubscribers().size(), is(equalTo(1)));
        assertThat(EventBus.getScoreUpdateSubscribers().contains(handlerWrapper), is(false));
    }

    @Test
    public void testPublishUnlockedPublishesGivenEvent() throws Exception {
        final String id = "test";
        final String title = "title";
        final String text = "text";
        final String score = "value";
        final int level = 3;
        final AchievementUnlockedEvent achievementUnlockedEvent = new AchievementUnlockedEvent(id, title, text, score, level);

        EventBus.publishUnlocked(achievementUnlockedEvent);

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
        final Score score = new Score(event, value);

        EventBus.publishScoreChanged(score);

        assertThat(receivedScore, notNullValue());
        assertThat(receivedScore.getEvent(), is(equalTo(event)));
        assertThat(receivedScore.getValue(), is(equalTo(value)));
    }

    @Test
    public void testTriggerEventWithScoreCallsControllerMethod() throws Exception {
        final String event = "test";
        final long score = 1L;

        EventBus.triggerEvent(event, score);

        verify(mockController).triggerEvent(event, score);
    }

    @Test
    public void testTriggerEventWithOwnersCallsControllerMethod() throws Exception {
        final String event = "test";
        final ArrayList<String> owners = new ArrayList<>();

        EventBus.triggerEvent(event, owners);

        verify(mockController).triggerEvent(event, owners);
    }

    @Test
    public void testCheckAllCallsControllerMethod() throws Exception {
        EventBus.checkAll();
        verify(mockController).unlockAllUnlockable();
    }
}
