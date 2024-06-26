package dev.fabled.astra.utils.logger;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AstraLog {

    private static @NotNull final String PREFIX;
    private static @NotNull final String DEBUG_PREFIX;
    private static @NotNull final String DIVIDER;
    private static boolean allowFormatting;
    private static boolean debugMode;

    static {
        PREFIX = "[Astra] ";
        DEBUG_PREFIX = "[<yellow>i<reset>] <yellow>";
        DIVIDER = "-----------------------------------";
    }

    public static void onLoad() {
        onReload();
    }

    public static void onReload() {
        final YamlConfig config = Astra.getUtilities().getConfigYml();
        allowFormatting = config.options().getBoolean("console-logging.allow-formatting", true);
        debugMode = config.options().getBoolean("console-logging.debug-mode", false);
    }

    private static void send(@NotNull final AstraLogLevel level, final boolean debug, @NotNull final String... input) {
        if (debug && !debugMode) {
            return;
        }

        for (String s : input) {
            s = debugMode
                    ? PREFIX + DEBUG_PREFIX + level.color + s
                    : PREFIX + level.color + s;

            if (!allowFormatting) {
                s = MiniColor.CONSOLE.stripTags(s);
            }

            Bukkit.getConsoleSender().sendMessage(
                    MiniColor.CONSOLE.deserialize(s)
            );
        }
    }



    /**
     * Send any amount of strings to console
     * @see AstraLog#log(String...)
     * @param level {@link AstraLogLevel} the level to mark the string as
     * @param input {@link String} the string to log to console
     */
    public static void log(@NotNull final AstraLogLevel level, @NotNull final String... input) {
        send(level, false, input);
    }

    /**
     * Send any amount of strings to console if debug mode is enabled
     * @see AstraLog#log(AstraLogLevel, String...)
     * @param input {@link String}
     */
    public static void log(@NotNull final String... input) {
        send(AstraLogLevel.INFO, false, input);
    }

    /**
     * Send a list of strings to console
     * @see AstraLog#log(List)
     * @param level {@link AstraLogLevel}
     * @param input {@link List}
     */
    public static void log(@NotNull final AstraLogLevel level, @NotNull final List<String> input) {
        send(level, false, input.toArray(new String[0]));
    }

    /**
     * Send a list of strings to console
     * @see AstraLog#log(AstraLogLevel, List)
     * @param input {@link List<String>}
     */
    public static void log(@NotNull final List<String> input) {
        log(AstraLogLevel.INFO, input);
    }

    /**
     * Send a stacktrace to console
     * @param throwable {@link Throwable}
     */
    public static void log(@NotNull final Throwable throwable) {
        send(AstraLogLevel.ERROR, false, ThrowableUtils.toString(throwable));
    }

    /**
     * Send a stacktrace to console
     * @param level {@link AstraLogLevel} the level to mark the stacktrace as
     * @param throwable {@link Throwable} the throwable that contains the stacktrace
     * @see AstraLog#log(Throwable)
     */
    public static void log(@NotNull final AstraLogLevel level, @NotNull final Throwable throwable) {
        send(level, false, ThrowableUtils.toString(throwable));
    }



    /**
     * Send any amount of strings to console if debug mode is enabled
     * @see AstraLog#debug(String...)
     * @param level {@link AstraLogLevel}
     * @param input {@link String}
     */
    public static void debug(@NotNull final AstraLogLevel level, @NotNull final String... input) {
        send(level, true, input);
    }

    /**
     * Send any amount of strings to console if debug mode is enabled
     * @see AstraLog#debug(AstraLogLevel, String...)
     * @param input {@link String}
     */
    public static void debug(@NotNull final String... input) {
        send(AstraLogLevel.INFO, true, input);
    }

    /**
     * Send a list of strings to console if debug mode is enabled
     * @see AstraLog#debug(List)
     * @param level {@link AstraLogLevel}
     * @param input {@link List}
     */
    public static void debug(@NotNull final AstraLogLevel level, @NotNull final List<String> input) {
        send(level, true, input.toArray(new String[0]));
    }

    /**
     * Send a list of strings to console if debug mode is enabled
     * @see AstraLog#debug(AstraLogLevel, List)
     * @param input {@link List<String>}
     */
    public static void debug(@NotNull final List<String> input) {
        debug(AstraLogLevel.INFO, input);
    }

    /**
     * Print a Stacktrace to console if debug mode is enabled
     * @param throwable {@link Throwable}
     */
    public static void debug(@NotNull final Throwable throwable) {
        send(AstraLogLevel.INFO, true, ThrowableUtils.toString(throwable));
    }



    /**
     * Sends a divider to console
     * @see AstraLog#divider()
     * @see AstraLog#divider(AstraLogLevel)
     * @see AstraLog#divider(boolean)
     * @param level {@link AstraLogLevel}
     * @param debug {@link Boolean}<br>If true, the divider will only be sent if debug mode is enabled
     */
    public static void divider(@NotNull final AstraLogLevel level, final boolean debug) {
        send(level, debug, DIVIDER);
    }

    /**
     * Sends a divider to console
     * @see AstraLog#divider()
     * @see AstraLog#divider(boolean)
     * @see AstraLog#divider(AstraLogLevel, boolean)
     * @param level {@link AstraLogLevel}
     */
    public static void divider(@NotNull final AstraLogLevel level) {
        divider(level, false);
    }

    /**
     * Sends a divider to console
     * @see AstraLog#divider()
     * @see AstraLog#divider(AstraLogLevel)
     * @see AstraLog#divider(AstraLogLevel, boolean)
     * @param debug {@link Boolean}<br>If true, the divider will only be sent if debug mode is enabled
     */
    public static void divider(final boolean debug) {
        divider(AstraLogLevel.INFO, debug);
    }

    /**
     * Sends a divider to console
     * @see AstraLog#divider(AstraLogLevel)
     * @see AstraLog#divider(boolean)
     * @see AstraLog#divider(AstraLogLevel, boolean)
     */
    public static void divider() {
        divider(AstraLogLevel.INFO, false);
    }

}
