package net.csongradyp.steps;

import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;

import javax.inject.Inject;

@Steps
public class ScoreAchievementUnlockedSteps {

    @Inject
    private Badger badger;

    @BeforeScenario
    public void beforeScoreAchievementUnlockedSteps() {
        badger.reset();
    }

    @When("event named $event is triggered $times times")
    public void triggerTimes(final @Named("event") String event, final @Named("trigger") Long times) {
        for (int i = 0; i < times; i++) {
            badger.triggerEvent(event);
        }
    }
}
