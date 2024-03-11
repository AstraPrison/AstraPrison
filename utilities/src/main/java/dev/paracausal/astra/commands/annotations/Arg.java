package dev.paracausal.astra.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Arg {

    /**
     * Determines if this argument is required or not<br>
     * Defaults to false (required)
     * @return boolean
     */
    boolean isOptional() default false;

    /**
     * The message sent to the player when they are missing this argument
     * @return String
     */
    String selectInputMessage() default "";

    /**
     * The message sent when the player inputs an invalid input for this argument<br>
     * Use '{ARG}' to input what they wrote into the message
     * @return String
     */
    String invalidInputMessage() default "";

}
