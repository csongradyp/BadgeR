package net.csongradyp.bdd.steps;

import java.util.Optional;
import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.provider.unlock.provider.TimeRangeUnlockedProvider;
import net.csongradyp.badger.provider.unlock.provider.TimeUnlockedProvider;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.provider.TestDateProvider;
import net.csongradyp.bdd.provider.TriggerChecker;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Steps
public class TimeAchievementUnlockedSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private TriggerChecker triggerChecker;
    @Inject
    private TimeUnlockedProvider timeUnlockedProvider;
    @Inject
    private TimeRangeUnlockedProvider timeRangeUnlockedProvider;
    @Inject
    private TestDateProvider dateProvider;

    @Given("current time is $time")
    public void setCurrentTime(final String time) {
        dateProvider.stubTime(time);
        timeUnlockedProvider.setDateProvider(dateProvider);
        timeRangeUnlockedProvider.setDateProvider(dateProvider);
        assertThat(dateProvider.currentTime(), is(equalTo(time)));
    }

    @Given("an achievement with $id id and $type type bounded to $event event with start trigger $start and end trigger $end")
    public void checkAchievementExistence(final @Named("id") String id, final @Named("type") AchievementType type, final @Named("event") String event, final @Named("start") String start, final @Named("end") String end) {
        final Optional<IAchievement> achievement = controller.get(type, id);
        if(achievement.isPresent()) {
            assertThat("Achievement is subscribed to event: " + event, achievement.get().getEvent().contains(event), is(true));
            assertThat("Trigger is present for time-range achievement", triggerChecker.isTimeRangeTriggerPresent(achievement.get(), start, end), is(true));
        } else {
            fail("Achievement is not defined with id: " + id + "and type: " + type);
        }
    }

}
