package dev.paracausal.astra.api.actions;

import dev.paracausal.astra.api.actions.impl.ConsoleCommandAction;
import dev.paracausal.astra.api.actions.impl.MessageAction;
import dev.paracausal.astra.api.actions.impl.PlayerCommandAction;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionManagerImpl implements ActionManager {

    private final Pattern actionPattern;
    private final Map<String, Action> actions;
    private Action defAction;

    public ActionManagerImpl() {
        actionPattern = Pattern.compile("^(\\[)(.*?)(])");
        actions = new HashMap<>();
    }

    public void onLoad() {
        defAction = new ConsoleCommandAction();
        register(defAction);
        register(new PlayerCommandAction());
        register(new MessageAction());
    }

    @Override
    public void register(@NotNull final Action action) {
        actions.putIfAbsent(action.id, action);
    }

    @Override
    public boolean exists(@NotNull final String id) {
        return actions.containsKey(id);
    }

    @Override
    public @Nullable Action getDefaultAction() {
        return defAction;
    }

    @Override
    public @Nullable Action getAction(@NotNull final String id) {
        return actions.getOrDefault(id, null);
    }

    @Override
    public @Nullable Action getFromCommandLine(@NotNull final String commandLine) {
        final Matcher matcher = actionPattern.matcher(commandLine);
        if (!matcher.find()) {
            return defAction;
        }

        return actions.getOrDefault(matcher.group(2), defAction);
    }

    @Override
    public @NotNull String replaceCommandLine(@NotNull String commandLine) {
        final Matcher matcher = actionPattern.matcher(commandLine);
        if (!matcher.find()) {
            return commandLine;
        }

        commandLine = commandLine.substring(matcher.end());

        if (commandLine.startsWith(" ")) {
            commandLine = commandLine.substring(1);
        }

        return commandLine;
    }

    @Override
    public void runCommandLine(@NotNull final OfflinePlayer player, @NotNull String commandLine) {
        final Action action = getFromCommandLine(commandLine);
        if (action == null) {
            return;
        }

        commandLine = replaceCommandLine(commandLine);
        action.run(player, new ActionArgument(commandLine));
    }

    @Override
    public void runCommandLine(@NotNull final OfflinePlayer player, @NotNull final List<String> commandLines) {
        commandLines.forEach(commandLine -> runCommandLine(player, commandLine));
    }

}
