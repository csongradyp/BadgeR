package net.csongradyp.bdd.steps;

import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.handler.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.csongradyp.badger.persistence.EventDao;
import net.csongradyp.bdd.Steps;
import org.jbehave.core.annotations.*;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Steps
public class EventTriggerSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private EventDao eventDao;
    private ScoreUpdatedEvent receivedEvent;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void beforeAchievementEventTriggerSteps() {
        controller.reset();
        receivedEvent = null;
    }

    @Given("there is subscription for score events")
    public void subscribeOnScoreEvents() {
        EventBus.subscribeOnScoreChanged(new ScoreUpdateHandlerWrapper(score -> receivedEvent = score));
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
        assertThat(receivedEvent, notNullValue());
    }

    @Then("no score event is received")
    public void noEventIsReceived() {
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
