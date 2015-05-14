package net.csongradyp.bdd.steps;

import net.csongradyp.bdd.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.bdd.provider.TestDateProvider;

import javax.inject.Inject;

@Steps
public class TimeRangeAchievementUnlockedSteps {

    @Inject
    private Badger badger;

    private TestDateProvider timeProvider = new TestDateProvider();



}
