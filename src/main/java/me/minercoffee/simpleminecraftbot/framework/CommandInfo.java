package me.minercoffee.simpleminecraftbot.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();

    String[] aliases() default {""};

    String permission() default "";

    String description() default "";

    boolean consoleOnly() default false;

    boolean playerOnly() default false;

    boolean disabled() default false;

    String[] shortCommands() default {""};

    String[] args() default {""};
}