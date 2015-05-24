package net.csongradyp.bdd.steps;

import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.provider.unlock.provider.CompositeUnlockedProvider;
import net.csongradyp.badger.provider.unlock.provider.DateUnlockedProvider;
import net.csongradyp.badger.provider.unlock.provider.TimeRangeUnlockedProvider;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.provider.TestDateProvider;
import net.csongradyp.bdd.provider.TriggerChecker;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@Steps
public class CompositeAchievementUnlockedSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private TriggerChecker triggerChecker;
    @Inject
    private DateUnlockedProvider dateUnlockedProvider;
    @Inject
    private TimeRangeUnlockedProvider timeRangeUnlockedProvider;
    @Inject
    private CompositeUnlockedProvider compositeUnlockedProvider;
    @Inject
    private TestDateProvider dateProvider;


    @Given("has a child with $id id and $type type bounded to $event event with start trigger $start and end trigger $end")
    public void checkhildTimeRangekAchievementExistence(final @Named("id") String id, final @Named("event") String event, final @Named("start") String start, final @Named("end") String end) {
        final Optional<IAchievement> achievement = controller.get(AchievementType.COMPOSITE, id);
        if(achievement.isPresent()) {
            final CompositeAchievementBean composite = (CompositeAchievementBean) achievement.get();
            assertThat("Achievement is subscribed to event: " + event, composite.getEvent().contains(event), is(true));
            final Collection<ITrigger> triggers = composite.getTrigger().stream().filter(t -> t.getType()==AchievementType.TIME_RANGE).collect(Collectors.toList());
            assertThat("Trigger is present for time-range achievement", triggerChecker.isTimeRangeTriggerPresent(triggers, start, end), is(true));
        } else {
            fail("Achievement is not defined with id: " + id + "and type: composite");
        }
    }

    @Given("current date is $date and current time is $time")
    public void setCurrentDateTime(final String date, final String time) {
        dateProvider.stubDateTime(date, time);
        assertThat(dateProvider.currentDateString(), is(equalTo(date)));
        dateUnlockedProvider.setDateProvider(dateProvider);
        timeRangeUnlockedProvider.setDateProvider(dateProvider);
        compositeUnlockedProvider.setDateProvider(dateProvider);
    }

}
