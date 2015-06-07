package net.csongradyp.bdd.steps.annotation;

import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.annotations.EventName;
import net.csongradyp.badger.annotations.EventTrigger;
import net.csongradyp.badger.annotations.TriggerValue;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.data.ScoreWrapper;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Steps
public class AnnotationEventTriggerSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private EventBus eventBus;
    @Resource
    private List<ScoreUpdatedEvent> scoreEventList;

    @AfterScenario(uponType = ScenarioType.ANY)
    public void unsubscribeOnScoreChangedListeners() {
        eventBus.unSubscribeAllOnScoreChanged();
    }


    @When("event named sample triggered via annotation")
    public void triggerSampleEvent() {
        sampleTriggered();
    }

    @EventTrigger(events = "sample")
    private void sampleTriggered() {
    }


    @When("event named $eventName is triggered via annotation")
    public void triggerViaAnnotation(final String eventName) {
        annotationTrigger(eventName);
    }

    @EventTrigger
    private void annotationTrigger(@EventName final String event) {
    }

    @When("$eventName event is triggered via annotation with $score as a $scoreType given with $parameter parameter")
    @Alias("<event> event is triggered via annotation with <input-score> as a <score-type>  given with <parameter> parameter")
    public void triggerScoreViaAnnotation(final @Named("event") String eventName, final @Named("input-score") Long score, final @Named("score-type") String scoreType, final @Named("parameter") String parameter) {
        if (scoreType.equals("highscore")) {
            if(parameter.equals("annotation")) {
                annotationHighScoreTrigger(eventName, score);
            } else {
                final ScoreWrapper scoreWrapper = new ScoreWrapper( score );
                annotationObjectHighScoreTrigger( eventName, scoreWrapper);
            }
        } else {
            if(parameter.equals("annotation")) {
                annotationTrigger(eventName, score);
            } else {
                final ScoreWrapper scoreWrapper = new ScoreWrapper( score );
                annotationObjectTrigger( eventName, scoreWrapper );
            }
        }
    }

    @EventTrigger
    private void annotationTrigger(@EventName final String event, @TriggerValue Long score) {
    }

    @EventTrigger
    private void annotationObjectTrigger(@EventName final String event, @TriggerValue(getter = "getScore") ScoreWrapper score) {
    }

    @EventTrigger(highScore = true)
    private void annotationHighScoreTrigger(@EventName final String event, @TriggerValue Long score) {
    }

    @EventTrigger(highScore = true)
    private void annotationObjectHighScoreTrigger(@EventName final String event, @TriggerValue(getter = "getScore") ScoreWrapper score) {
    }

    @Then("the number of received score event is $number")
    public void eventIsReceived(final Integer number) {
        assertThat(scoreEventList.size(), is(equalTo(number)));
    }

}
