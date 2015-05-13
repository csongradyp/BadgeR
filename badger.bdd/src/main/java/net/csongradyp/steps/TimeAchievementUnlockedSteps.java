package net.csongradyp.steps;

import net.csongrady.badger.domain.AchievementType;
import net.csongrady.badger.domain.IAchievement;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.provider.TestDateProvider;
import net.csongradyp.provider.TriggerChecker;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;

import javax.inject.Inject;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Steps
public class TimeAchievementUnlockedSteps {

    @Inject
    private Badger badger;
    @Inject
    private TriggerChecker triggerChecker;

    private TestDateProvider timeProvider = new TestDateProvider();

    @Given("current time is $time")
    public void setCurrentTime(final String time) {
        timeProvider.stubTime(time);
        assertThat(timeProvider.currentTime(), is(equalTo(time)));
        badger.getController().setDateProvider(timeProvider);
    }

    @Given("an achievement with $id id and $type type bounded to $event event with start trigger $start and end trigger $end")
    public void checkAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type, final @Named("event") String event, final @Named("start") String start, final @Named("end") String end) {
        final Optional<IAchievement> achievement = badger.getAchievement(type, id);
        if(achievement.isPresent()) {
            assertThat("Achievement is subscribed to event: " + event, achievement.get().getEvent().contains(event), is(true));
            assertThat("Trigger is present for time-range achievement", triggerChecker.isTimeRangeTriggerPresent(achievement.get(), start, end), is(true));
        } else {
            fail("Achievement is not defined with id: " + id + "and type: " + type);
        }
    }

}
