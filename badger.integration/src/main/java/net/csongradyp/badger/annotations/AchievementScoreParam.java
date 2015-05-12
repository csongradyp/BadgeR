package net.csongradyp.badger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter annotation to mark achievement score information holders.
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.PARAMETER)
public @interface AchievementScoreParam {

    /**
     * If the parameter is not a {@link Long} instance then a getter method should be called to get the score information.
     *
     * @return Method to be called to get the score information as a {@link Long}.
     */
    String getter() default "";
}
