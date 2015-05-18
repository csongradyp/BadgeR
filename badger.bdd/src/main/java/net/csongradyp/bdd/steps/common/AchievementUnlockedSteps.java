package net.csongradyp.bdd.steps.common;

import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.handler.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.provider.TriggerChecker;
import org.jbehave.core.annotations.*;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

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
        final Optional<IAchievement> achievement = controller.get(type, id);
        if(achievement.isPresent()) {
            assertThat("Achievement is subscribed to event" + event, achievement.get().getEvent().contains(event), is(true));
            assertThat("Trigger:" + trigger +" is present for achievement", triggerChecker.isTriggerPresent(achievement.get(), type, trigger), is(true));
        } else {
            fail("Achievement is not defined with id: " + id + "and type: " + type);
        }
    }

    @Given("an achievement with $id id and $type type")
    public void checkCompositeAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type) {
        final Optional<IAchievement> achievement = controller.get(type, id);
       assertThat("Achievement is not defined with id: " + id + "and type: " + type, achievement.isPresent(), is(true));
    }

    @Given("has a child with $id id and $type type bounded to $event event with trigger $trigger")
    public void checkChildAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type, final @Named("event") String event, final @Named("trigger") String trigger) {
        final Optional<IAchievement> achievement = controller.get(AchievementType.COMPOSITE, id);
        if(achievement.isPresent()) {
            final CompositeAchievementBean composite = (CompositeAchievementBean) achievement.get();
            assertThat("Achievement is subscribed to event" + event, composite.getEvent().contains(event), is(true));
            final IAchievement child = composite.getChildren().get(type);
            assertThat("Trigger:" + trigger +" is present for achievement", triggerChecker.isTriggerPresent(child, type, trigger), is(true));
        } else {
            fail("Achievement is not defined with id: " + id + "and type: composite ");
        }
    }

    @Given("the achievement with $id id is already unlocked with level $level")
    public void unlock(final @Named("id") String id, final @Named("level") Integer level) {
        achievementDao.unlock(id, level);
        assertThat(controller.isUnlocked(id, level), is(true));
    }

    @When("an achievement with $id id is unlocked")
    public void unlockManually(final @Named("id") String id) {
        controller.unlock(AchievementType.SINGLE, id, "");
        assertThat(controller.isUnlocked(id), is(true));
    }

    @Given("the achievement with $id is not unlocked")
    @Alias("the achievement with <id> is not unlocked")
    public void checkAchievementUnlocked(final @Named("id") String id) {
        assertThat(controller.isUnlocked(id), is(false));
    }

    @Then("achievement unlocked event is received")
    public void unlockedEventIsReceived() {
        assertThat(eventList.isEmpty(), is(false));
        receivedEvent = eventList.get(0);
        eventList.clear();
    }

    @Then("unlocked event received for achievement $id")
    public void unlockedEventReceived(final String id) {
        assertThat(eventList.isEmpty(), is(false));
        final Optional<IAchievementUnlockedEvent> relatedEvent = eventList.stream().filter(event -> event.getId().equals(id)).findAny();
        assertThat(relatedEvent.isPresent(), is(true));
        receivedEvent = relatedEvent.get();
    }

    @Then("no achievement unlocked event received")
    public void noUnlockedEventIsReceived() {
        assertThat(eventList.isEmpty(), is(true));
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
