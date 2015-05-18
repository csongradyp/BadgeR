package net.csongradyp.bdd.steps;

import net.csongradyp.badger.AchievementController;
import net.csongradyp.bdd.Steps;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;

import javax.inject.Inject;

@Steps
public class ScoreAchievementUnlockedSteps {

    @Inject
    private AchievementController controller;

    @BeforeScenario
    public void beforeScoreAchievementUnlockedSteps() {
        controller.reset();
    }

    @When("event named $event is triggered $times times")
    public void triggerTimes(final @Named("event") String event, final @Named("trigger") Long times) {
        for (int i = 0; i < times; i++) {
            controller.triggerEvent(event);
        }
    }
}
