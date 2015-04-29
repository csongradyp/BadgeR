package com.noe.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for set the score a specific counter (event).
 * After annotated method execution Badger will set the new given score of
 * the given counter and will check for achievements that may have triggered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AchievementScore {

    /**
     * Name of counter or event.
     */
    String counter();

    String[] owners() default {};

}
