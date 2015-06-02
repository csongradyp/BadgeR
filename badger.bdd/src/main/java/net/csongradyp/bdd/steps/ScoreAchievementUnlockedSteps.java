package net.csongradyp.bdd.steps;

import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.annotations.AchievementEventParam;
import net.csongradyp.badger.annotations.AchievementEventTrigger;
import net.csongradyp.badger.persistence.EventDao;
import net.csongradyp.bdd.Steps;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class ScoreAchievementUnlockedSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private EventDao eventDao;

    @BeforeScenario
    public void beforeScoreAchievementUnlockedSteps() {
        controller.reset();
    }

    @When("event named $event is triggered $times times")
    public void triggerTimes(final String event, final Long times) {
        for (int i = 0; i < times; i++) {
            controller.triggerEvent(event);
        }
    }

    @When("event named $event is triggered $times times via annotation")
    public void annotationTriggerTimes(final String event, final Long times) {
        for (int i = 0; i < times; i++) {
            final Long currentValue = eventDao.scoreOf(event);
            annotationTrigger(event);
            final Long newValue = eventDao.scoreOf(event);
            assertThat("Annotation trigger incremented event counter:", newValue, is(equalTo(currentValue + 1)));
        }
    }

    @AchievementEventTrigger
    private void annotationTrigger(@AchievementEventParam final String event) {
    }
}
