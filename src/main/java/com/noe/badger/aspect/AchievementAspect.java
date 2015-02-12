package com.noe.badger.aspect;

import com.noe.badger.AchievementController;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.inject.Inject;

@Aspect
public class AchievementAspect {

    @Inject
    private AchievementController achievementController;

    @Pointcut(value = "@annotation(achievementCounterIncrement)", argNames = "achievementCounterIncrement")
    public void increment(AchievementCounterIncrement achievementCounterIncrement) {
        final String counterId = achievementCounterIncrement.counter();
        achievementController.incrementCounter(counterId);
    }

}
