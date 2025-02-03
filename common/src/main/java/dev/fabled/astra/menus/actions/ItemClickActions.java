package dev.fabled.astra.menus.actions;

import dev.fabled.astra.api.actions.AbstractItemClickAction;
import dev.fabled.astra.api.actions.ClickActionManager;
import dev.fabled.astra.api.actions.ClickActionManagerImpl;
import dev.fabled.astra.api.actions.ItemClickActionArgs;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemClickActions {

    private final @NotNull Map<String, ItemClickActionCommandLines> clickActions;

    public ItemClickActions(
            final @NotNull YamlConfig config,
            @NotNull String key
    )
            throws NullPointerException
    {
        final List<String> fullActionLines = ListUtils.fromConfig(config, key, new ArrayList<>());
        if (fullActionLines.isEmpty()) {
            throw new NullPointerException("This click type has no actions!");
        }

        clickActions = new HashMap<>();
        addClickActions(fullActionLines);
    }

    public ItemClickActions(final @NotNull Map<String, ItemClickActionCommandLines> clickActions) {
        this.clickActions = new HashMap<>(clickActions);
    }

    public ItemClickActions() {
        clickActions = new HashMap<>();
    }

    public @NotNull Map<String, ItemClickActionCommandLines> getClickActions() {
        return new HashMap<>(clickActions);
    }

    public @NotNull ItemClickActions addClickActions(final @NotNull String actionId, final @NotNull List<String> input) {
        if (!ClickActionManagerImpl.getInstance().exists(actionId)) {
            return this;
        }

        clickActions.putIfAbsent(actionId, new ItemClickActionCommandLines());
        clickActions.get(actionId).addCommandLines(input);
        return this;
    }

    public @NotNull ItemClickActions addClickActions(final @NotNull String actionId, final @NotNull String... input) {
        return addClickActions(actionId, List.of(input));
    }

    public @NotNull ItemClickActions addClickActions(final @NotNull List<String> input) {
        final ClickActionManagerImpl clickActionManager = ClickActionManagerImpl.getInstance();

        input.forEach(string -> {
            final AbstractItemClickAction action = clickActionManager.getActionFromString(string);
            final String actionId = action.getId();
            clickActions.putIfAbsent(actionId, new ItemClickActionCommandLines());
            string = ClickActionManager.removeActionIdFromString(actionId, string);
            clickActions.get(actionId).addCommandLines(string);
        });

        return this;
    }

    public @NotNull ItemClickActions addClickActions(final @NotNull String... input) {
        return addClickActions(List.of(input));
    }

    public void run(final @NotNull Player player) {
        final ClickActionManagerImpl manager = ClickActionManagerImpl.getInstance();
        clickActions.forEach((actionId, commandLines) -> {
            final AbstractItemClickAction action = manager.getAction(actionId);
            if (action == null) {
                return;
            }

            commandLines.getCommandLines().forEach(commandLine -> action.run(player, new ItemClickActionArgs(commandLine)));
        });
    }

}
