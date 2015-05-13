package net.csongradyp.steps.common;

import javax.inject.Inject;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.badger.persistence.CounterDao;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class EventScoreSteps {

    @Inject
    private Badger badger;
    @Inject
    private CounterDao counterDao;

    @Given("the current $event event score is $score")
    @Alias("the current <event> event score is <current-score>")
    public void score(final @Named("event") String eventName, final @Named("score") Long score) {
        counterDao.setScore(eventName, score);
        assertThat(counterDao.scoreOf(eventName), is(equalTo(score)));
    }

    @When("event named $eventName is triggered")
    public void trigger(final String eventName) {
        badger.triggerEvent(eventName);
    }
}
