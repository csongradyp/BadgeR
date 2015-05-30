package net.csongradyp.bdd.steps.common;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.provider.TriggerChecker;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Steps
public class AchievementUnlockedSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private AchievementDao achievementDao;
    @Inject
    private TriggerChecker triggerChecker;
    @Resource
    private List<IAchievementUnlockedEvent> eventList;

    private IAchievementUnlockedEvent receivedEvent;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void clearEvents() {
        eventList.clear();
    }

    @Given("there is subscription for achievement unlocked events")
    public void subscribeOnUnlockEvents() {
        EventBus.subscribeOnUnlock(new AchievementUnlockedHandlerWrapper(eventList::add));
    }

    @Given("an achievement with $id id and $type type bounded to $event event with trigger $trigger")
    public void checkAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type, final @Named("event") String event, final @Named("trigger") String trigger) {
        final Optional<IAchievement> achievement = controller.get(id);
        if (achievement.isPresent()) {
            assertThat("Achievement is subscribed to event" + event, achievement.get().getEvent().contains(event), is(true));
            assertThat("Trigger:" + trigger + " is present for achievement", triggerChecker.isTriggerPresent((ITriggerableAchievementBean) achievement.get(), type, trigger), is(true));
        } else {
            fail("Achievement is not defined with id: " + id + "and type: " + type);
        }
    }

    @Given("an achievement with $id id and $type type")
    public void checkCompositeAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type) {
        final Optional<IAchievement> achievement = controller.get(id);
        assertThat("Achievement is not defined with id: " + id + "and type: " + type, achievement.isPresent(), is(true));
    }

    @Given("an achievement with $id id is subscribed to $event event")
    public void checkAchievementSubscriptionExistence(final @Named("id") String id, final @Named("event") String event) {
        final Optional<IAchievement> achievement = controller.get(id);
        if (achievement.isPresent()) {
            assertThat("Achievement is subscribed to event: " + event, achievement.get().getEvent().contains(event), is(true));
        } else {
            fail("Achievement is not defined with id: " + id);
        }
    }

    @Given("an achievement with $id id and $type type bounded to $event event with start trigger $start and end trigger $end")
    public void checkAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type, final @Named("event") String event, final @Named("start") String start, final @Named("end") String end) {
        final Optional<IAchievement> achievement = controller.get(id);
        if (achievement.isPresent()) {
            final IAchievement iAchievement = achievement.get();
            if (iAchievement.getType() == AchievementType.TIME_RANGE) {
                TimeRangeAchievementBean timeRangeAchievementBean = (TimeRangeAchievementBean) iAchievement;
                assertThat("Achievement is subscribed to event: " + event, timeRangeAchievementBean.getEvent().contains(event), is(true));
                assertThat("Trigger is present for time-range achievement", triggerChecker.isTimeRangeTriggerPresent(timeRangeAchievementBean.getTrigger(), start, end), is(true));
            } else if (iAchievement.getType() == AchievementType.SCORE_RANGE) {
                ScoreRangeAchievementBean scoreRangeAchievementBean = (ScoreRangeAchievementBean) iAchievement;
                final Optional<ScoreTriggerPair> matchingTrigger = scoreRangeAchievementBean.getTrigger().stream().filter(t -> t.getStartTrigger().equals(Long.valueOf(start)) && t.getEndTrigger().equals(Long.valueOf(end))).findAny();
                assertThat("Trigger is present for score-range achievement " + event, matchingTrigger.isPresent(), is(true));
            }
        } else {
            fail("Achievement is not defined with id: " + id + " and type: " + type);
        }
    }

    @Given("the achievement with $id id is already unlocked with level $level")
    public void unlockWithLevel(final @Named("id") String id, final @Named("level") Integer level) {
        achievementDao.unlock(id, level);
        assertThat(achievementDao.isUnlocked(id, level), is(true));
    }

    @Given("the achievement with $id id is unlocked")
    public void unlock(final @Named("id") String id) {
        achievementDao.unlock(id);
        assertThat(achievementDao.isUnlocked(id), is(true));
    }

    @When("an achievement with $id id is unlocked")
    public void unlockManually(final @Named("id") String id) {
        controller.unlock(id, "");
        assertThat(controller.isUnlocked(id), is(true));
    }

    @Given("the achievement with $id is not unlocked")
    @Alias("the achievement with <id> is not unlocked")
    public void checkAchievementUnlocked(final @Named("id") String id) {
        assertThat(controller.isUnlocked(id), is(false));
    }

    @Then("unlocked event received for achievement $id")
    public void unlockedEventReceived(final String id) {
        assertThat(eventList.isEmpty(), is(false));
        final Optional<IAchievementUnlockedEvent> relatedEvent = eventList.stream().filter(event -> event.getId().equals(id)).findAny();
        assertThat(relatedEvent.isPresent(), is(true));
        receivedEvent = relatedEvent.get();
    }

    @Then("no achievement unlocked event received related to $id")
    public void noUnlockedEventIsReceived(final String id) {
        assertThat(eventList.stream().filter(e -> e.getId().equals(id)).findAny().isPresent(), is(false));
    }

    @Then("achievement id is $id")
    public void checkAchievementId(final @Named("id") String id) {
        assertThat(receivedEvent.getId(), is(equalTo(id)));
    }

    @Then("the level of the unlocked achievement is $level")
    public void checkAchievementLevel(final @Named("level") Integer level) {
        assertThat(receivedEvent.getLevel(), is(equalTo(level)));
    }

    @AsParameterConverter
    public AchievementType getType(final String type) {
        return AchievementType.parse(type);
    }

}
