package dev.fabled.astra.utils.logger;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ThrowableUtils {

    public static @NotNull String toString(@NotNull final Throwable throwable) {
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);

        throwable.printStackTrace(printWriter);
        printWriter.flush();

        return writer.toString();
    }

}
