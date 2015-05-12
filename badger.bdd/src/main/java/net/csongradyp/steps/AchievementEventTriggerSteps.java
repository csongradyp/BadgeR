package net.csongradyp.steps;

import javax.inject.Inject;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.badger.event.message.Score;
import net.csongradyp.badger.persistence.CounterDao;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Steps
public class AchievementEventTriggerSteps {

    @Inject
    private Badger badger;
    @Inject
    private CounterDao counterDao;
    private Score receivedEvent;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void beforeAchievementEventTriggerSteps() {
        badger.reset();
        receivedEvent = null;
    }

    @Given("there is subscription for score events")
    public void subscribeOnScoreEvents() {
        badger.subscribeOnScoreChanged(score -> receivedEvent = score);
    }

    @When("$eventName event is triggered with $score as a $scoreType")
    @Alias("<event> event is triggered with <input-score> as a <score-type>")
    public void trigger(final @Named("event") String eventName, final @Named("input-score") Long score, final @Named("score-type") String scoreType) {
        if (scoreType.equals("highscore")) {
            badger.triggerEventWithHighScore(eventName, score);
        } else {
            badger.triggerEvent(eventName, score);
        }
    }

    @Then("the score event is received")
    public void eventIsReceived() {
        assertThat(receivedEvent, notNullValue());
    }

    @Then("no event is received")
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
        assertThat(counterDao.scoreOf(eventName), is(equalTo(score)));
    }


}
