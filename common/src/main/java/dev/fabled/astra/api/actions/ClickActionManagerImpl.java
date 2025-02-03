package dev.fabled.astra.api.actions;

import dev.fabled.astra.api.actions.impl.ConsoleCommandClickAction;
import dev.fabled.astra.api.actions.impl.MessageClickAction;
import dev.fabled.astra.api.actions.impl.PlayerCommandClickAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ClickActionManagerImpl implements ClickActionManager {

    private static ClickActionManagerImpl instance;
    private static final @NotNull Pattern PATTERN;
    private static final @NotNull Set<AbstractItemClickAction> DEFAULT_ACTIONS;
    private static final @NotNull String DEFAULT_ACTION_ID;

    static {
        PATTERN = Pattern.compile("^(\\[)(.*?)(])");
        DEFAULT_ACTIONS = Set.of(
                new ConsoleCommandClickAction(),
                new PlayerCommandClickAction(),
                new MessageClickAction()
        );
        DEFAULT_ACTION_ID = "console";
    }

    public static ClickActionManagerImpl getInstance() {
        if (instance == null) {
            instance = new ClickActionManagerImpl();
        }

        return instance;
    }

    private final @NotNull Map<String, AbstractItemClickAction> actions;

    private ClickActionManagerImpl() {
        actions = new HashMap<>();
        DEFAULT_ACTIONS.forEach(this::register);
    }

    @Override
    public void register(final @NotNull AbstractItemClickAction action) {
        actions.putIfAbsent(action.getId(), action);
    }

    @Override
    public @NotNull Boolean exists(final @NotNull String id) {
        return actions.containsKey(id);
    }

    @Override
    public @Nullable AbstractItemClickAction getAction(final @NotNull String id) {
        return actions.getOrDefault(id, null);
    }

    public AbstractItemClickAction getDefaultAction() {
        if (!exists("console")) {
            register(new ConsoleCommandClickAction());
        }

        return actions.get(DEFAULT_ACTION_ID);
    }

    @Override
    public @NotNull AbstractItemClickAction getActionFromString(final @NotNull String fullCommandLine) {
        final Matcher matcher = PATTERN.matcher(fullCommandLine);
        if (!matcher.find()) {
            return getDefaultAction();
        }

        final String group = matcher.group(2);
        if (group == null || !exists(group)) {
            return getDefaultAction();
        }

        return actions.get(group);
    }

    @Override
    public @NotNull List<String> getActionIDs() {
        return new ArrayList<>(actions.keySet());
    }

    @Override
    public @NotNull String removeActionIdFromString(final @NotNull String fullCommandLine) {
        if (!fullCommandLine.startsWith("[")) {
            return fullCommandLine;
        }

        final AbstractItemClickAction action = getActionFromString(fullCommandLine);
        String commandLine = fullCommandLine.substring(action.getId().length() + 2);
        if (commandLine.startsWith(" ")) {
            commandLine = commandLine.substring(1);
        }

        return commandLine;
    }

}
