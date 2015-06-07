package net.csongradyp.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for trigger a specific event.
 * After annotated method execution Badger will increment the counter or set the new given score of
 * of the event and will check for achievements that may have triggered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventTrigger {

    /**
     * Name of event to trigger.
     */
    String[] events() default {};

    String[] owners() default {};

    boolean highScore() default false;
}
