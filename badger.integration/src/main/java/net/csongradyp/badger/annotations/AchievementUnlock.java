package net.csongradyp.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to unlock the given achievement and trigger achievement unlocked event.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AchievementUnlock {

    /**
     * IDs of achievement to unlock.
     */
    String[] achievements() default {};

    /**
     * Value the achievement unlock with.
     * @return The value for the achievement for record.
     */
    String triggerValue() default "";

    String[] owners() default {};
}
