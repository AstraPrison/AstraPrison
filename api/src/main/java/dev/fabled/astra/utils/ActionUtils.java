package dev.fabled.astra.utils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionUtils {

    private static final Pattern PATTERN;

    static {
        PATTERN = Pattern.compile("^(\\[)([^]]*)(])?(.*)");
    }

    public static @NotNull String getIdFromCommandLine(final @NotNull String commandLine) {
        final Matcher matcher = PATTERN.matcher(commandLine);
        if (!matcher.matches()) {
            return "console";
        }

        try { return matcher.group(2); }
        catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return "console";
        }
    }

}
