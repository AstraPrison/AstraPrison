package dev.fabled.astra.utils.parsers;

import dev.fabled.astra.utils.logger.AstraLog;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumberParser {

    @Contract(pure = true)
    public static @Nullable <T extends Number> T parse(@NotNull final String input, @NotNull final Parser<T> parser, @Nullable final T def) {
        try { return parser.parse(input); }
        catch (NumberFormatException e) {
            AstraLog.debug(e);
        }

        return def;
    }

    @Contract(pure = true)
    public static @NotNull <T extends Number> T parseOrDefault(@NotNull final String input, @NotNull final Parser<T> parser, @NotNull final T def) {
        try { return parser.parse(input); }
        catch (NumberFormatException e) {
            AstraLog.debug(e);
        }

        return def;
    }

}
