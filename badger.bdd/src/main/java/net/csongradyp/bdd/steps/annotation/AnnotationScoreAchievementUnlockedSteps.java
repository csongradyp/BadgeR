package net.csongradyp.bdd.steps.annotation;

import net.csongradyp.badger.annotations.EventName;
import net.csongradyp.badger.annotations.EventTrigger;
import net.csongradyp.badger.persistence.EventDao;
import net.csongradyp.bdd.Steps;
import org.jbehave.core.annotations.When;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class AnnotationScoreAchievementUnlockedSteps {

    @Inject
    private EventDao eventDao;

    @When("event named $event is triggered $times times via annotation")
    public void annotationTriggerTimes(final String event, final Long times) {
        for (int i = 0; i < times; i++) {
            final Long currentValue = eventDao.scoreOf(event);
            annotationTrigger(event);
            final Long newValue = eventDao.scoreOf(event);
            assertThat("Annotation trigger incremented event counter:", newValue, is(equalTo(currentValue + 1)));
        }
    }

    @EventTrigger
    private void annotationTrigger(@EventName final String event) {
    }
}
