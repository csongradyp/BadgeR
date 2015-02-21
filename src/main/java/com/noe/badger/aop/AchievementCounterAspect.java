package com.noe.badger.aop;

import com.noe.badger.event.EventBus;
import com.noe.badger.event.domain.IncrementEvent;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class AchievementCounterAspect {

    Logger LOG = LoggerFactory.getLogger( AchievementCounterAspect.class );

    @Pointcut( value = "execution(* *(..)) && @annotation(achievementCounterIncrement)", argNames = "achievementCounterIncrement" )
    public void defineEntryPoint(final AchievementCounterIncrement achievementCounterIncrement) {
    }

    @After( "defineEntryPoint(achievementCounterIncrement)" )
    public void increment(final AchievementCounterIncrement achievementCounterIncrement) {
        final String id = achievementCounterIncrement.counter();
        LOG.debug( "Achievement check: " + id );
        EventBus.incrementAndCheck(new IncrementEvent(id));
    }
}
