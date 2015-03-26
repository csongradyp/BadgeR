package com.noe.badger.aop;//package com.noe.badger.aop;

import com.noe.badger.annotations.AchievementCheck;
import com.noe.badger.annotations.AchievementEventTrigger;
import com.noe.badger.annotations.AchievementScore;
import com.noe.badger.annotations.AchievementUnlock;
import com.noe.badger.event.EventBus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class AchievementCounterAspect {

    Logger LOG = LoggerFactory.getLogger(AchievementCounterAspect.class);

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementCheck)", argNames = "achievementCheck")
    public void checkEntryPoint(final AchievementCheck achievementCheck) {
    }

    @After(value = "checkEntryPoint(achievementCheck)", argNames = "achievementCheck")
    public void check(final AchievementCheck achievementCheck) {
        LOG.debug("Checking for achievements");
        EventBus.checkAll();
    }

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementUnlock)", argNames = "achievementUnlock")
    public void unlockEntryPoint(final AchievementUnlock achievementUnlock) {
    }

    @After(value = "unlockEntryPoint(achievementUnlock)", argNames = "achievementUnlock")
    public void unlock(final AchievementUnlock achievementUnlock) {
        final String id = achievementUnlock.Id();
        final String triggerValue = achievementUnlock.triggerValue();
        LOG.debug("Achievement unlock: {}", id);
        EventBus.unlock(id, triggerValue);
    }

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementEventTrigger)", argNames = "achievementEventTrigger")
    public void triggerEntryPoint(final AchievementEventTrigger achievementEventTrigger) {
    }

    @After(value = "triggerEntryPoint(achievementEventTrigger)", argNames = "achievementEventTrigger")
    public void trigger(final AchievementEventTrigger achievementEventTrigger) {
        final String event = achievementEventTrigger.name();
        LOG.debug("Achievement event triggered: {}", event);
        EventBus.triggerEvent(event);
    }

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementScore)", argNames = "achievementScore")
    public void scoreEntryPoint(final AchievementScore achievementScore) {
    }

    @After(value = "scoreEntryPoint(achievementScore)", argNames = "joinPoint, achievementScore")
    public void score(final JoinPoint joinPoint, final AchievementScore achievementScore) {
        final String id = achievementScore.counter();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String[] parameterNames = signature.getParameterNames();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();
        for (int i = 0, parameterNamesLength = parameterNames.length; i < parameterNamesLength; i++) {
            if (achievementScore.scoreParam().equals(parameterNames[i]) && (long.class.equals(parameterTypes[i]) || Long.class.equals(parameterTypes[i]))) {
                final Long score = (Long) parameterValues[i];
                EventBus.triggerEvent(id, score);
            }
        }
    }

}
