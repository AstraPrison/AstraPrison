package dev.fabled.astra.menus;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MenuClickType {

    LEFT("left", ClickType.LEFT),
    SHIFT_LEFT("shift-left", ClickType.SHIFT_LEFT),
    RIGHT("right", ClickType.RIGHT),
    SHIFT_RIGHT("shift-right", ClickType.SHIFT_RIGHT),
    DOUBLE_CLICK("double", ClickType.DOUBLE_CLICK),
    SWAP_OFFHAND("off-hand", ClickType.SWAP_OFFHAND),
    DROP("drop", ClickType.DROP),
    CONTROL_DROP("control-drop", ClickType.CONTROL_DROP),
    NUMBER_KEY("number-key", ClickType.NUMBER_KEY),
    ANY("any");

    private final @NotNull String key;
    private final @Nullable ClickType clickType;

    MenuClickType(final @NotNull String key, final @Nullable ClickType clickType) {
        this.key = key;
        this.clickType = clickType;
    }

    MenuClickType(final @NotNull String key) {
        this(key, null);
    }

    public @NotNull String getKey() {
        return key;
    }

    public @NotNull ClickType getClickType() {
        return clickType == null ? ClickType.LEFT : clickType;
    }

    public static MenuClickType fromClickType(final @NotNull ClickType clickType) {
        return switch (clickType) {
            case ClickType.LEFT -> LEFT;
            case ClickType.SHIFT_LEFT -> SHIFT_LEFT;
            case ClickType.RIGHT -> RIGHT;
            case ClickType.SHIFT_RIGHT -> SHIFT_RIGHT;
            case ClickType.DOUBLE_CLICK -> DOUBLE_CLICK;
            case ClickType.SWAP_OFFHAND -> SWAP_OFFHAND;
            case ClickType.DROP -> DROP;
            case ClickType.CONTROL_DROP -> CONTROL_DROP;
            case ClickType.NUMBER_KEY -> NUMBER_KEY;
            default -> ANY;
        };
    }

}
