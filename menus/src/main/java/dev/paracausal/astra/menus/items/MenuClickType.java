package dev.paracausal.astra.menus.items;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public enum MenuClickType {

    ANY(ClickType.values()),
    CONTROL_DROP(ClickType.CONTROL_DROP),
    DOUBLE_CLICK(ClickType.DOUBLE_CLICK),
    DROP(ClickType.DROP),
    LEFT(ClickType.LEFT),
    MIDDLE(ClickType.MIDDLE),
    NUMBER_KEY(ClickType.NUMBER_KEY),
    RIGHT(ClickType.RIGHT),
    SHIFT_LEFT(ClickType.SHIFT_LEFT),
    SHIFT_RIGHT(ClickType.SHIFT_RIGHT),
    SWAP_OFFHAND(ClickType.SWAP_OFFHAND),
    WINDOW_BORDER_LEFT(ClickType.WINDOW_BORDER_LEFT),
    WINDOW_BORDER_RIGHT(ClickType.WINDOW_BORDER_RIGHT);

    private final ClickType[] clickTypes;

    MenuClickType(@NotNull final ClickType... clickTypes) {
        this.clickTypes = clickTypes;
    }

    public String getConfigPath() {
        return toString().toLowerCase().replace("_", "-");
    }

}
