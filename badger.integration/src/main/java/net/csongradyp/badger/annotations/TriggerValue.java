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
public @interface TriggerValue {

      String getter() default "";
}
