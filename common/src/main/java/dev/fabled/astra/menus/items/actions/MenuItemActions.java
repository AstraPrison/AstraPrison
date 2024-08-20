package dev.fabled.astra.menus.items.actions;

import dev.fabled.astra.api.actions.ActionArgument;
import dev.fabled.astra.menus.items.MenuClickType;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MenuItemActions {

    private final @NotNull List<ActionArgument> actions;

    public MenuItemActions() {
        actions = new ArrayList<>();
    }

    public MenuItemActions(final @NotNull MenuClickType clickType, final @NotNull YamlConfig config, @NotNull String path) {
        if (!path.endsWith(".")) {
            path += ".";
        }

        actions = ListUtils.fromConfig(config, path + clickType.getPath())
                .stream().map(ActionArgument::new)
                .collect(Collectors.toList());
    }

    public @NotNull List<ActionArgument> getActions() {
        return new ArrayList<>(actions);
    }

}
