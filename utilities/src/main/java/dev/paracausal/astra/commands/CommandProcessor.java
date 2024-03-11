package dev.paracausal.astra.commands;

import dev.paracausal.astra.commands.annotations.Arg;
import dev.paracausal.astra.commands.annotations.ArgumentSubCommand;
import dev.paracausal.astra.commands.annotations.NotSender;
import dev.paracausal.astra.exceptions.InvalidCommandMethodException;
import dev.paracausal.astra.logger.AstraLog;
import dev.paracausal.astra.logger.AstraLogLevel;
import dev.paracausal.astra.utilities.MiniColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class CommandProcessor {

    private enum ParameterType {
        PLAYER, OFFLINE_PLAYER, COMMAND_SENDER,
        CHARACTER, STRING, STRING_ARRAY,
        INTEGER, LONG, FLOAT, DOUBLE;

        private static @NotNull ParameterType fromClass(@NotNull final Class<?> clazz) {
            if (clazz.equals(Player.class)) return PLAYER;
            if (clazz.equals(OfflinePlayer.class)) return OFFLINE_PLAYER;
            if (clazz.equals(CommandSender.class) || clazz.equals(ConsoleCommandSender.class)) return COMMAND_SENDER;
            if (clazz.equals(Character.class) || clazz.equals(char.class)) return CHARACTER;
            if (clazz.equals(Integer.class) || clazz.equals(int.class)) return INTEGER;
            if (clazz.equals(Long.class) || clazz.equals(long.class)) return LONG;
            if (clazz.equals(Float.class) || clazz.equals(float.class)) return FLOAT;
            if (clazz.equals(Double.class) || clazz.equals(double.class)) return DOUBLE;
            if (clazz.equals(String[].class)) return STRING_ARRAY;
            return STRING;
        }
    }



    static void process(
            @NotNull final AstraCommand command,
            @NotNull final CommandMethod commandMethod,
            @NotNull final CommandSender sender,
            @NotNull final String[] args
    )
            throws InvalidCommandMethodException
    {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        final boolean isPlayer = player != null;

        final Method method = commandMethod.method;
        final Parameter[] parameters = method.getParameters();
        final List<Object> parameterValues = new ArrayList<>();

        int argPos = 1;
        if (method.isAnnotationPresent(ArgumentSubCommand.class)) {
            argPos = 0;
        }

        for (final Parameter parameter : parameters) {
            final ParameterType type = ParameterType.fromClass(parameter.getType());
            final boolean isArg = parameter.isAnnotationPresent(Arg.class);

            if (type == ParameterType.PLAYER && !isArg) {
                if (!isPlayer) {
                    throw new InvalidCommandMethodException("The method cannot be a Console method and have a non-arg Player parameter!");
                }

                parameterValues.add(player);
                continue;
            }

            if (type == ParameterType.COMMAND_SENDER && !isArg) {
                parameterValues.add(sender);
                continue;
            }

            if (!isArg) {
                AstraLog.debug(AstraLogLevel.ERROR, "Parameter '" + parameter.getName() + "' is not an arg or sender!");
                continue;
            }

            // if length <= argPos then we are missing an argument at argPos
            final boolean notEnoughArgs = args.length <= argPos;

            final boolean notSender = parameter.isAnnotationPresent(NotSender.class)
                    && (type == ParameterType.PLAYER || type == ParameterType.OFFLINE_PLAYER);

            final Arg arg = parameter.getAnnotation(Arg.class);

            final boolean isOptional = notSender && arg.isOptional();
            final String selectInputMessage = arg.selectInputMessage();
            final String invalidInputMessage = arg.invalidInputMessage();

            if (notEnoughArgs && !isOptional) {
                sendMessage(sender, selectInputMessage);
                return;
            }

            if (type == ParameterType.PLAYER) {
                if (isOptional) {
                    if (isPlayer) {
                        parameterValues.add(player);
                        continue;
                    }

                    else {
                        sendMessage(sender, selectInputMessage);
                        return;
                    }
                }

                final Player target = Bukkit.getPlayer(args[argPos]);
                if (target == null) {
                    sendMessage(sender, invalidInputMessage.replace("{ARG}", args[argPos]));
                    return;
                }

                if (isPlayer && notSender && target == player) {
                    sender.sendMessage("You cannot specify yourself!");
                    return;
                }

                parameterValues.add(target);
                argPos++;
                continue;
            }

            if (type == ParameterType.OFFLINE_PLAYER) {
                if (isOptional) {
                    if (isPlayer) {
                        parameterValues.add(player);
                        continue;
                    }

                    else {
                        sendMessage(sender, selectInputMessage);
                        return;
                    }
                }

                final OfflinePlayer target = Bukkit.getOfflinePlayer(args[argPos]);
                if (isPlayer && notSender && target.isOnline() && target.getPlayer() == player) {
                    sender.sendMessage("You cannot specify yourself!");
                    return;
                }

                if (!target.isOnline() && !target.hasPlayedBefore()) {
                    sendMessage(sender, invalidInputMessage.replace("{ARG}", args[argPos]));
                    return;
                }

                parameterValues.add(target);
                argPos++;
                continue;
            }

            if (type == ParameterType.STRING || type == ParameterType.CHARACTER) {
                if (isOptional) {
                    continue;
                }

                if (type == ParameterType.CHARACTER) {
                    parameterValues.add(args[argPos].charAt(0));
                    argPos++;
                    continue;
                }

                parameterValues.add(args[argPos]);
                argPos++;
                continue;
            }

            if (type == ParameterType.STRING_ARRAY) {
                if (isOptional) {
                    break;
                }

                parameterValues.add(Arrays.copyOfRange(args, argPos, args.length));
                break;
            }

            if (type == ParameterType.INTEGER || type == ParameterType.LONG) {
                if (isOptional) {
                    continue;
                }

                try {
                    if (type == ParameterType.INTEGER) parameterValues.add(Integer.parseInt(args[argPos]));
                    else parameterValues.add(Long.parseLong(args[argPos]));
                    argPos++;
                }

                catch (NumberFormatException e) {
                    sendMessage(sender, invalidInputMessage.replace("{ARG}", args[argPos]));
                    return;
                }

                continue;
            }

            if (type == ParameterType.FLOAT) {
                if (isOptional) {
                    continue;
                }

                try { parameterValues.add(Float.parseFloat(args[argPos])); }
                catch (NumberFormatException e) {
                    sendMessage(sender, invalidInputMessage.replace("{ARG}", args[argPos]));
                    return;
                }

                argPos++;
                continue;
            }

            assert type == ParameterType.DOUBLE;
            if (isOptional) {
                continue;
            }

            try { parameterValues.add(Double.parseDouble(args[argPos])); }
            catch (NumberFormatException e) {
                sendMessage(sender, invalidInputMessage.replace("{ARG}", args[argPos]));
                return;
            }
        }

        try { method.invoke(command, parameterValues.toArray()); }
        catch (Exception e) {
            throw new RuntimeException("An error occurred while running the " + command.getName() + " command!");
        }
    }

    private static void sendMessage(@NotNull final CommandSender sender, @NotNull final String inputMessage) {
        if (inputMessage.length() < 1) {
            return;
        }

        if (sender instanceof Player player) {
            player.sendMessage(MiniColor.ALL.deserialize(inputMessage));
            return;
        }

        AstraLog.log(AstraLogLevel.ERROR, inputMessage);
    }

}
