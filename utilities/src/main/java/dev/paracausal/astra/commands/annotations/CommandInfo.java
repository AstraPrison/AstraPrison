package dev.paracausal.astra.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    String commandName();
    String[] aliases() default {};
    String permission() default "";
    String usage() default "";
    String description() default "";

}
