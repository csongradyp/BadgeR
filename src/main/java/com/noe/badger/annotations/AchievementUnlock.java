package com.noe.badger.annotations;

import com.noe.badger.AchievementType;

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
     * Type of the achievement.
     * @return {@link com.noe.badger.AchievementType}
     */
    AchievementType type() default AchievementType.SINGLE;

    /**
     * ID of achievement to unlock.
     */
    String Id();

    String triggerValue() default "";

}
