package net.csongradyp.steps.common;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import net.csongrady.badger.domain.AchievementType;
import net.csongrady.badger.domain.IAchievement;
import net.csongrady.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.data.ReceivedAchievementUnlockedEvents;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.Then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Steps
public class AchievementUnlockedSteps {

    @Inject
    private Badger badger;
    @Inject
    private AchievementDao achievementDao;
    @Inject
    private ReceivedAchievementUnlockedEvents receivedEvent;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void clearEvents() {
        receivedEvent.clear();
    }

    @Given("there is subscription for achievement unlocked events")
    public void subscribeOnUnlockEvents() {
        badger.subscribeOnUnlock(receivedEvent::add);
    }

    @Given("an achievement with $id id and $type type bounded to $event event with trigger $trigger")
    public void checkAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type, final @Named("event") String event, final @Named("trigger") Long trigger) {
        Set<IAchievement> definedAchievements = badger.getAllAchievementByEvent().get(event);
        if (definedAchievements != null) {
            final Optional<IAchievement> definedAchievement = definedAchievements.stream().filter(achievement -> achievement.getType().equals(type)).findAny();
            if (definedAchievement.isPresent()) {
                assertThat(definedAchievement.get().getType(), is(equalTo(type)));
                assertThat(definedAchievement.get().getEvent().contains(event), is(true));
            }
        } else {
            fail("Achievement is not defined with id: " + id + "and type: " + type);
        }
    }

    @Given("the achievement with $id id is already unlocked with level $level")
    public void unlock(final @Named("id") String id, final @Named("level") Integer level) {
        achievementDao.unlock(id, level);
        assertThat(badger.isUnlocked(id, level), is(true));
    }

    @Given("the achievement with $id is not unlocked")
    @Alias("the achievement with <id> is not unlocked")
    public void checkAchievementUnlocked(final @Named("id") String id) {
        assertThat(badger.isUnlocked(id), is(false));
    }


    @Then("achievement unlocked event is received")
    public void unlockedEventIsReceived() {
        assertThat(receivedEvent.isEmpty(), is(false));
    }

    @Then("achievement id is $id")
    public void checkAchievementId(final @Named("id") String id) {
        final List<IAchievementUnlockedEvent> events = receivedEvent.getAll();
//        assertThat(events.size(), is(equalTo(1)));
        assertThat(events.get(0).getId(), is(equalTo(id)));
    }

    @Then("the level of the unlocked achievement is $level")
    public void checkAchievementLevel(final @Named("level") Integer level) {
        final List<IAchievementUnlockedEvent> events = receivedEvent.getAll();
//        assertThat(events.size(), is(equalTo(1)));
        assertThat(events.get(0).getLevel(), is(equalTo(level)));
    }

    @AsParameterConverter
    public AchievementType getType(String type) {
        return AchievementType.parse(type);
    }

}
