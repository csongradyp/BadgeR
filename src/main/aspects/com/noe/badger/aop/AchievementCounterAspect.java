package com.noe.badger.aop;//package com.noe.badger.aop;

import com.noe.badger.event.EventBus;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class AchievementCounterAspect {

    Logger LOG = LoggerFactory.getLogger(AchievementCounterAspect.class);

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementUnlock)", argNames = "achievementUnlock")
    public void unlockEntryPoint(final AchievementUnlock achievementUnlock) {
    }

    @After(value = "unlockEntryPoint(achievementUnlock)", argNames = "achievementUnlock")
    public void unlock(final AchievementUnlock achievementUnlock) {
        final String id = achievementUnlock.Id();
        LOG.debug("Achievement unlock: {}", id);
        EventBus.unlock(id);
    }

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementEventTrigger)", argNames = "achievementEventTrigger")
    public void incrementEntryPoint(final AchievementEventTrigger achievementEventTrigger) {
    }

    @After(value = "incrementEntryPoint(achievementEventTrigger)", argNames = "achievementEventTrigger")
    public void increment(final AchievementEventTrigger achievementEventTrigger) {
        final String event = achievementEventTrigger.name();
        LOG.debug("Achievement event triggered: {}", event);
        EventBus.triggerEvent(event);
    }

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementScore)", argNames = "achievementScore")
    public void scoreEntryPoint(final AchievementScore achievementScore) {
    }

    @After(value = "scoreEntryPoint(achievementScore)", argNames = "achievementScore")
    public void score(final AchievementScore achievementScore) {
        final String id = achievementScore.counter();
        final String score = achievementScore.score();
        LOG.debug("Achievement new score {} of {}", score, id);
        EventBus.scoreAndCheck(id, Long.valueOf(score));
    }
}
