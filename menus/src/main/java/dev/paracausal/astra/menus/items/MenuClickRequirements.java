package dev.paracausal.astra.menus.items;

import dev.paracausal.astra.utilities.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MenuClickRequirements extends MenuRequirements {

    private @NotNull final MenuClickType clickType;

    /**
     * Create menu click requirements from a menu config item's path!
     * @param clickType The ClickType to search for
     * @param config The menu's YamlConfig
     * @param path The item's path<br>Example: 'contents.{id}'
     */
    public MenuClickRequirements(
            @NotNull final MenuClickType clickType,
            @NotNull final YamlConfig config,
            @NotNull String path
    ) {
        super(config, (path.endsWith(".") ? path : path + ".") + clickType.getConfigPath());
        this.clickType = clickType;
    }

    /**
     * Create menu click requirements from a pre-made map of click requirements!
     * @param clickType The MenuClickType
     * @param clickRequirements the map (of key String and value MenuRequirement) to use
     */
    public MenuClickRequirements(@NotNull final MenuClickType clickType, @NotNull final Map<String, MenuRequirement> clickRequirements) {
        super(clickRequirements);
        this.clickType = clickType;
    }

    public @NotNull MenuClickType getClickType() {
        return clickType;
    }

}
