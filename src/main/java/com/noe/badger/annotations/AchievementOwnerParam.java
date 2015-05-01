package com.noe.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter annotation to mark achievement owner information holders.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AchievementOwnerParam {

    /**
     * If the parameter is not a {@link String} instance then a getter method should be called to get the owner information.
     *
     * @return Method to be called to get the owner information as a {@link String}.
     */
    String getter() default "";
}
