package net.csongradyp.badger.aop;

import java.util.List;
import javax.inject.Inject;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.annotations.AchievementEventTrigger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TriggerAspect extends AchievementAspect {

    @Inject
    private IAchievementController achievementController;

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementEventTrigger)", argNames = "achievementEventTrigger")
    public void triggerEntryPoint(final AchievementEventTrigger achievementEventTrigger) {
    }

    @After(value = "triggerEntryPoint(achievementEventTrigger)", argNames = "joinPoint, achievementEventTrigger")
    public void trigger(final JoinPoint joinPoint, final AchievementEventTrigger achievementEventTrigger) {
        final List<String> owners = collectOwners(joinPoint, achievementEventTrigger.owners());
        final List<String> events = collectEvents(joinPoint, achievementEventTrigger.eventNames());
        for (String event : events) {
            achievementController.triggerEvent(event, owners);
        }
    }
}
