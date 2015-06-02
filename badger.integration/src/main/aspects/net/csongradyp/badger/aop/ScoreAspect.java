package net.csongradyp.badger.aop;

import java.util.List;
import javax.inject.Inject;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.annotations.AchievementScore;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ScoreAspect extends AchievementAspect {

    @Inject
    private IAchievementController achievementController;

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementScore)", argNames = "achievementScore")
    public void scoreEntryPoint(final AchievementScore achievementScore) {
    }

    @After(value = "scoreEntryPoint(achievementScore)", argNames = "joinPoint, achievementScore")
    public void score(final JoinPoint joinPoint, final AchievementScore achievementScore) {
        final List<String> owners = collectOwners(joinPoint, achievementScore.owners());
        final Long score = getScore(joinPoint);
        final List<String> events = collectEvents(joinPoint, achievementScore.events());
        for (String event : events) {
            achievementController.triggerEvent(event, score, owners);
        }
    }
}
