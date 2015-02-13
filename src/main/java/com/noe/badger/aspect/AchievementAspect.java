package com.noe.badger.aspect;

import com.noe.badger.AchievementController;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.inject.Inject;
import javax.inject.Named;

@Aspect
@Named
public class AchievementAspect {

    @Inject
    private AchievementController achievementController;

    @Pointcut(value = "@annotation(achievementCounterIncrement)", argNames = "achievementCounterIncrement")
    public void incrementPointCut(final AchievementCounterIncrement achievementCounterIncrement) {
        achievementCounterIncrement.counter();
    }

    @After("incrementPointCut(achievementCounterIncrement)")
    public void increment(final AchievementCounterIncrement achievementCounterIncrement){
        final String counterId = achievementCounterIncrement.counter();
        achievementController.incrementAndCheck(counterId);
    }

    @Pointcut(value = "@annotation(achievementScore)", argNames = "achievementScore")
    public void score(AchievementScore achievementScore) {
        final String counterId = achievementScore.counter();
        final String score = achievementScore.score();
        achievementController.setScoreAndCheck(counterId, Long.parseLong(score));
    }

}
