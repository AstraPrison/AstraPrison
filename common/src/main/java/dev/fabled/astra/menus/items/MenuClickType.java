package dev.fabled.astra.menus.items;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MenuClickType {

    LEFT(ClickType.LEFT),
    SHIFT_LEFT(ClickType.SHIFT_LEFT),
    RIGHT(ClickType.RIGHT),
    SHIFT_RIGHT(ClickType.SHIFT_RIGHT),
    NUMBER_KEY(ClickType.NUMBER_KEY),
    DOUBLE_CLICK(ClickType.DOUBLE_CLICK),
    DROP(ClickType.DROP),
    CONTROL_DROP(ClickType.CONTROL_DROP),
    SWAP_OFFHAND(ClickType.SWAP_OFFHAND),
    WINDOW_BORDER_LEFT(ClickType.WINDOW_BORDER_LEFT),
    WINDOW_BORDER_RIGHT(ClickType.WINDOW_BORDER_RIGHT),
    ANY;

    private final @Nullable ClickType clickType;
    private final @NotNull String path;

    MenuClickType(final @Nullable ClickType clickType) {
        this.clickType = clickType;
        path = toString().toLowerCase().replace("_", "-");
    }

    MenuClickType() {
        this(null);
    }

    public @Nullable ClickType getClickType() {
        return clickType;
    }

    public boolean isClickType(final @NotNull ClickType clickType) {
        return this.clickType == null || this.clickType == clickType;
    }

    public @NotNull String getPath() {
        return path;
    }

    public static @NotNull MenuClickType getFromClickType(final @NotNull ClickType clickType) {
        MenuClickType result = MenuClickType.ANY;

        for (final MenuClickType menuClickType : MenuClickType.values()) {
            if (menuClickType.clickType == clickType) {
                result = menuClickType;
                break;
            }
        }

        return result;
    }

    public static @Nullable MenuClickType getFromString(@NotNull String clickType) {
        try { return MenuClickType.valueOf(clickType.toUpperCase().replace("-", "_")); }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

}
