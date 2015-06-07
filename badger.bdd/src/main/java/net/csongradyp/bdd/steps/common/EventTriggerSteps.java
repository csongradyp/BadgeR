package net.csongradyp.bdd.steps.common;

import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.csongradyp.badger.persistence.EventDao;
import net.csongradyp.bdd.Steps;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Steps
public class EventTriggerSteps {

    @Inject
    private EventBus eventBus;
    @Inject
    private AchievementController controller;
    @Inject
    private EventDao eventDao;
    @Resource
    private List<ScoreUpdatedEvent> scoreEventList;
    private ScoreUpdatedEvent receivedEvent;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void beforeAchievementEventTriggerSteps() {
        controller.reset();
        scoreEventList.clear();
        receivedEvent = null;
    }

    @AfterScenario(uponType = ScenarioType.ANY)
    public void unsubscribeOnScoreChangedListeners() {
        eventBus.unSubscribeAllOnScoreChanged();
    }

    @Given("there is subscription for score events")
    public void subscribeOnScoreEvents() {
        eventBus.subscribeOnScoreChanged(new ScoreUpdateHandlerWrapper(scoreEventList::add));
    }


    @Given("the current $event event score is $score")
    @Alias("the current <event> event score is <current-score>")
    public void score(final @Named("event") String eventName, final @Named("score") Long score) {
        eventDao.setScore(eventName, score);
        assertThat(eventDao.scoreOf(eventName), is(equalTo(score)));
    }

    @When("event named $eventName is triggered")
    public void trigger(final String eventName) {
        controller.triggerEvent(eventName);
    }

    @When("$eventName event is triggered with $score as a $scoreType")
    @Alias("<event> event is triggered with <input-score> as a <score-type>")
    public void trigger(final @Named("event") String eventName, final @Named("input-score") Long score, final @Named("score-type") String scoreType) {
        if (scoreType.equals("highscore")) {
            controller.triggerEventWithHighScore(eventName, score);
        } else {
            controller.triggerEvent(eventName, score);
        }
    }

    @Then("the score event is received")
    public void eventIsReceived() {
        assertThat(scoreEventList.size(), is(equalTo(1)));
        receivedEvent = scoreEventList.remove(0);
        scoreEventList.clear();
    }

    @Then("no score event is received")
    public void noEventIsReceived() {
        assertThat(scoreEventList.isEmpty(), is(true));
        assertThat(receivedEvent, nullValue());
    }

    @Then("is related to $event")
    @Alias("is related to <event>")
    public void checkEventName(final @Named("event") String event) {
        assertThat(receivedEvent.getEvent(), is(equalTo(event)));
    }

    @Then("the value of the score is $expectedScore")
    @Alias("the value of the score is <expected-score>")
    public void checkScoreValue(final @Named("expected-score") Long expectedScore) {
        assertThat(receivedEvent.getValue(), is(equalTo(expectedScore)));
    }

    @Then("the current $event event score is $score")
    public void checkScore(final @Named("event") String eventName, final @Named("score") Long score) {
        assertThat(eventDao.scoreOf(eventName), is(equalTo(score)));
    }


}
