package dev.paracausal.astra.menus.items;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record MenuClickActions(@NotNull MenuClickType clickType, @NotNull List<String> commandLines) {

    public MenuClickActions(@NotNull final MenuClickType clickType, @NotNull final List<String> commandLines) {
        this.clickType = clickType;
        this.commandLines = new ArrayList<>(commandLines);
    }

}
