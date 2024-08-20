package dev.fabled.astra.api.actions;

import dev.fabled.astra.api.actions.impl.ConsoleCommandAction;
import dev.fabled.astra.api.actions.impl.MenuAction;
import dev.fabled.astra.api.actions.impl.PlayerCommandAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManagerImpl implements ActionManager {

    private final @NotNull Map<String, AstraAction> actions;

    public ActionManagerImpl() {
        actions = new HashMap<>();
    }

    public void onEnable() {
        new ConsoleCommandAction().register();
        new PlayerCommandAction().register();
        new MenuAction().register();
    }

    @Override
    public @NotNull String getDefaultActionId() {
        return ConsoleCommandAction.ID;
    }

    @Override
    public void register(@NotNull AstraAction action) {
        actions.putIfAbsent(action.getID(), action);
    }

    @Override
    public boolean exists(@NotNull String id) {
        return actions.containsKey(id);
    }

    @Override
    public @Nullable AstraAction getAction(@NotNull String id) {
        return actions.getOrDefault(id, null);
    }

    @Override
    public @NotNull List<String> getActionIDs() {
        return new ArrayList<>(actions.keySet());
    }

}
