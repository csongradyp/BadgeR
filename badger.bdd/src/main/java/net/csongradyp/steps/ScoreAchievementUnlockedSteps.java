package net.csongradyp.steps;

import javax.inject.Inject;
import net.csongrady.badger.domain.AchievementType;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.data.ReceivedAchievementUnlockedEvents;
import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class ScoreAchievementUnlockedSteps {

    @Inject
    private Badger badger;
    @Inject
    private AchievementDao achievementDao;
    @Inject
    private ReceivedAchievementUnlockedEvents receivedEvent;

    @BeforeScenario
    public void beforeScoreAchievementUnlockedSteps() {
        receivedEvent = null;
        badger.reset();
    }

    @Given("the achievement with $id id is already unlocked with level $level")
    public void checkAchievementUnlocked(final @Named("id") String id, final @Named("level") Integer level) {
        achievementDao.unlock(id, level);
        assertThat(badger.isUnlocked(id, level), is(true));
        receivedEvent = null;
    }

    @When("event named $event is triggered $times times")
    public void triggerTimes(final @Named("event") String event, final @Named("trigger") Long times) {
        for (int i = 0; i < times; i++) {
            badger.triggerEvent(event);
        }
    }

    @AsParameterConverter
    public AchievementType getType(String type) {
        return AchievementType.parse(type);
    }
}
