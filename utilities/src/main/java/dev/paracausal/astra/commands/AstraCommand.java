package dev.paracausal.astra.commands;

import dev.paracausal.astra.commands.annotations.ArgumentSubCommand;
import dev.paracausal.astra.commands.annotations.CommandInfo;
import dev.paracausal.astra.commands.annotations.PlayerCommand;
import dev.paracausal.astra.commands.annotations.SubCommand;
import dev.paracausal.astra.exceptions.InvalidCommandException;
import dev.paracausal.astra.exceptions.InvalidCommandMethodException;
import dev.paracausal.astra.logger.AstraLog;
import dev.paracausal.astra.logger.AstraLogLevel;
import dev.paracausal.astra.utilities.MiniColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AstraCommand extends BukkitCommand {

    private CommandMethod baseConsoleMethod;
    private CommandMethod basePlayerMethod;

    private final Map<String, CommandMethod> consoleSubCommands;
    private final Map<String, CommandMethod> playerSubCommands;

    private CommandMethod playerArgumentSubCommand;
    private CommandMethod consoleArgumentSubCommand;

    protected AstraCommand() throws InvalidCommandException {
        super("");

        final Class<?> clazz = this.getClass();
        if (!clazz.isAnnotationPresent(CommandInfo.class)) {
            throw new InvalidCommandException("The command class " + clazz.getSimpleName() + " is not annotated properly!");
        }

        final CommandInfo info = clazz.getAnnotation(CommandInfo.class);

        if (info.commandName().length() < 1) {
            throw new InvalidCommandException("The command class " + clazz.getSimpleName() + " has no command name specified!");
        }

        setName(info.commandName());

        if (info.aliases().length > 0) {
            setAliases(List.of(info.aliases()));
        }

        if (info.permission().length() > 0) {
            setPermission(info.permission());
        }

        if (info.usage().length() > 0) {
            setUsage(info.usage());
        }

        if (info.description().length() > 0) {
            setDescription(info.description());
        }

        consoleSubCommands = new HashMap<>();
        playerSubCommands = new HashMap<>();

        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            final CommandMethod commandMethod;
            try { commandMethod = new CommandMethod(method); }
            catch (InvalidCommandMethodException e) {
                AstraLog.debug(AstraLogLevel.ERROR, e.getMessage());
                continue;
            }

            if (commandMethod.isBase) {
                if (commandMethod.isConsole) {
                    baseConsoleMethod = commandMethod;
                    continue;
                }

                basePlayerMethod = commandMethod;
                continue;
            }

            if (method.isAnnotationPresent(ArgumentSubCommand.class)) {
                if (commandMethod.isConsole) {
                    consoleSubCommands.clear();
                    consoleArgumentSubCommand = commandMethod;
                    return;
                }

                playerSubCommands.clear();
                playerArgumentSubCommand = commandMethod;
                return;
            }

            final SubCommand subCommandAnnotation = method.getAnnotation(SubCommand.class);
            String name = subCommandAnnotation.name();
            String[] aliases = subCommandAnnotation.aliases();

            if (commandMethod.isConsole) {
                consoleSubCommands.put(name.toLowerCase(), commandMethod);
                for (final String alias : aliases) {
                    consoleSubCommands.put(alias.toLowerCase(), commandMethod);
                }
                continue;
            }

            playerSubCommands.put(name.toLowerCase(), commandMethod);
            for (final String alias : aliases) {
                playerSubCommands.put(alias.toLowerCase(), commandMethod);
            }
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        final boolean isPlayer = player != null;
        CommandMethod commandMethod = null;

        if (args.length == 0) {
            if (isPlayer) commandMethod = basePlayerMethod;
            else commandMethod = baseConsoleMethod;
        }

        else if (isPlayer && playerArgumentSubCommand != null) {
            commandMethod = playerArgumentSubCommand;
        }

        else if (isPlayer && playerSubCommands.containsKey(args[0].toLowerCase())) {
            commandMethod = playerSubCommands.get(args[0].toLowerCase());
            final PlayerCommand playerCommand = commandMethod.method.getAnnotation(PlayerCommand.class);
            assert playerCommand != null;

            if (!player.hasPermission(playerCommand.value())) {
                if (playerCommand.message().length() > 0) {
                    player.sendMessage(MiniColor.ALL.deserialize(playerCommand.message()));
                }
                return true;
            }
        }

        else if (consoleArgumentSubCommand != null) {
            commandMethod = consoleArgumentSubCommand;
        }

        else if (consoleSubCommands.containsKey(args[0])) {
            commandMethod = consoleSubCommands.get(args[0]);
        }

        if (commandMethod != null) {
            try { CommandProcessor.process(this, commandMethod, sender, args); }
            catch (InvalidCommandMethodException e) {
                AstraLog.log(AstraLogLevel.ERROR, e.getMessage());
            }

            return true;
        }

        final CommandInfo commandInfo = this.getClass().getAnnotation(CommandInfo.class);
        if (commandInfo.usage().length() < 1) {
            return true;
        }

        if (isPlayer) {
            player.sendMessage(MiniColor.ALL.deserialize(commandInfo.usage()));
            return true;
        }

        AstraLog.log(commandInfo.usage());
        return true;
    }

    public abstract @NotNull List<String> tabComplete(@NotNull final Player player, @NotNull final String[] args);

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!(sender instanceof Player player)) {
            return new ArrayList<>();
        }

        return tabComplete(player, args).stream()
                .filter(string -> string.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

}
