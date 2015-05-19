package net.csongradyp.bdd.steps.common;

import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.persistence.EventDao;
import net.csongradyp.bdd.Steps;
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
    private AchievementController controller;
    @Inject
    private EventDao eventDao;

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
}
