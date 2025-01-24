package dev.fabled.astra.utils.logger;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AstraLog {

    private static final @NotNull String DEBUG_PREFIX;
    private static final @NotNull String DIVIDER;
    private static ComponentLogger componentLogger;
    private static boolean allowFormatting;
    private static boolean debugMode;

    static {
        DEBUG_PREFIX = "[<yellow>i<reset>] ";
        DIVIDER = "-----------------------------------";
    }

    public static void onLoad() {
        componentLogger = Astra.getPlugin().getComponentLogger();
        onReload();
    }

    public static void onReload() {
        final YamlConfig config = Astra.get().getConfigYml();
        allowFormatting = config.options().getBoolean("logging.allow-formatting", true);
        debugMode = config.options().getBoolean("logging.debug-mode", false);
    }



    private static void send(final @NotNull AstraLogLevel level, final boolean debug, final @NotNull List<String> input) {
        if (debug && !debugMode) {
            return;
        }

        for (String s : input) {
            s = debugMode
                    ? DEBUG_PREFIX + level.color + s
                    : level.color + s;

            if (!allowFormatting) {
                s = MiniColor.CONSOLE.stripTags(s);
            }

            componentLogger.info(
                    MiniColor.CONSOLE.deserialize(s)
            );
        }
    }

    private static void send(final @NotNull AstraLogLevel level, final boolean debug, final @NotNull String... input) {
        send(level, debug, List.of(input));
    }



    public static void log(final @NotNull AstraLogLevel level, final @NotNull List<String> input) {
        send(level, false, input);
    }

    public static void log(final @NotNull List<String> input) {
        send(AstraLogLevel.INFO, false, input);
    }

    public static void log(final @NotNull AstraLogLevel level, final @NotNull String... input) {
        send(level, false, input);
    }

    public static void log(final @NotNull String... input) {
        send(AstraLogLevel.INFO, false, input);
    }

    public static void log(final @NotNull AstraLogLevel level, final @NotNull Throwable throwable) {
        send(level, false, ThrowableUtils.getStackTrace(throwable));
    }

    public static void log(final @NotNull Throwable throwable) {
        send(AstraLogLevel.ERROR, false, ThrowableUtils.getStackTrace(throwable));
    }



    public static void debug(final @NotNull AstraLogLevel level, final @NotNull List<String> input) {
        send(level, true, input);
    }

    public static void debug(final @NotNull List<String> input) {
        send(AstraLogLevel.INFO, true, input);
    }

    public static void debug(final @NotNull AstraLogLevel level, final @NotNull String... input) {
        send(level, true, input);
    }

    public static void debug(final @NotNull String... input) {
        send(AstraLogLevel.INFO, true, input);
    }

    public static void debug(final @NotNull AstraLogLevel level, final @NotNull Throwable throwable) {
        send(level, true, ThrowableUtils.getStackTrace(throwable));
    }

    public static void debug(final @NotNull Throwable throwable) {
        send(AstraLogLevel.ERROR, true, ThrowableUtils.getStackTrace(throwable));
    }



    public static void divider(final @NotNull AstraLogLevel level, final boolean debug) {
        send(level, debug, DIVIDER);
    }

    public static void divider(final @NotNull AstraLogLevel level) {
        send(level, false, DIVIDER);
    }

    public static void divider(final boolean debug) {
        send(AstraLogLevel.INFO, debug, DIVIDER);
    }

    public static void divider() {
        send(AstraLogLevel.INFO, false, DIVIDER);
    }

}
