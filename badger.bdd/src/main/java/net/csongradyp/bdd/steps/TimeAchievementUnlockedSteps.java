package net.csongradyp.bdd.steps;

import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.provider.unlock.provider.TimeRangeUnlockedProvider;
import net.csongradyp.badger.provider.unlock.provider.TimeUnlockedProvider;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.provider.TestDateProvider;
import net.csongradyp.bdd.provider.TriggerChecker;
import org.jbehave.core.annotations.Given;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        assertThat(dateProvider.currentTimeString(), is(equalTo(time)));
    }

}
