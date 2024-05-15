package dev.fabled.astra.utils.logger;

import dev.fabled.astra.Astra;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

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

    public static void log(@NotNull final AstraLogLevel level, @NotNull final String... input) {
        send(level, false, input);
    }

    public static void log(@NotNull final String... input) {
        log(AstraLogLevel.INFO, input);
    }

    public static void debug(@NotNull final AstraLogLevel level, @NotNull final String... input) {
        send(level, true, input);
    }

    public static void debug(@NotNull final String... input) {
        debug(AstraLogLevel.INFO, input);
    }

    public static void divider(@NotNull final AstraLogLevel level, final boolean debug) {
        send(level, debug, DIVIDER);
    }

    public static void divider(@NotNull final AstraLogLevel level) {
        divider(level, false);
    }

    public static void divider(final boolean debug) {
        divider(AstraLogLevel.INFO, debug);
    }

    public static void divider() {
        divider(AstraLogLevel.INFO, false);
    }

}
