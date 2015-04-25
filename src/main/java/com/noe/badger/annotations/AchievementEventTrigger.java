package com.noe.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for trigger a specific event.
 * After annotated method execution Badger will increment the counter
 * of the given event and will check for achievements that may have triggered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AchievementEventTrigger {

    /**
     * Name of event to trigger.
     */
    String[] name();

    String[] owners() default {};

}
