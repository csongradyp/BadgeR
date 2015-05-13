package net.csongradyp.steps;

import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.provider.TestDateProvider;

import javax.inject.Inject;

@Steps
public class TimeRangeAchievementUnlockedSteps {

    @Inject
    private Badger badger;

    private TestDateProvider timeProvider = new TestDateProvider();



}
