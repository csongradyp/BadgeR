package com.noe.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for Achievement AOP Pointcut.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AchievementUnlock {

    /**
     * ID of achievement to unlock.
     */
    String achievement();


    /**
     * Value the achievement unlock with.
     * @return The value for the achievement for record.
     */
    String scoreParam() default "";

}
