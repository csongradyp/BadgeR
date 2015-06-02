package net.csongradyp.badger.aop;

import javax.inject.Inject;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.annotations.AchievementCheck;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CheckAspect {

    @Inject
    private IAchievementController achievementController;

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementCheck)", argNames = "achievementCheck")
    public void checkEntryPoint(final AchievementCheck achievementCheck) {
    }

    @After(value = "checkEntryPoint(achievementCheck)", argNames = "achievementCheck")
    public void check(final AchievementCheck achievementCheck) {
        achievementController.checkAndUnlock();
    }
}
