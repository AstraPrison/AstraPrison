package dev.fabled.astra.menus.actions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ItemClickActionCommandLines {

    private final List<String> commandLines;

    public ItemClickActionCommandLines() {
        commandLines = new ArrayList<>();
    }

    public @NotNull ItemClickActionCommandLines addCommandLines(final @NotNull List<String> commandLines) {
        this.commandLines.addAll(commandLines);
        return this;
    }

    public @NotNull ItemClickActionCommandLines addCommandLines(final @NotNull String... commandLines) {
        return addCommandLines(List.of(commandLines));
    }

    public @NotNull List<String> getCommandLines() {
        return new ArrayList<>(commandLines);
    }

}
