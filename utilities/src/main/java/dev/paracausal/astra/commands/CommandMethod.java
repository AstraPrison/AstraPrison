package dev.paracausal.astra.commands;

import dev.paracausal.astra.commands.annotations.*;
import dev.paracausal.astra.exceptions.InvalidCommandMethodException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final class CommandMethod {

    final Method method;
    final boolean isBase;
    final boolean isConsole;

    CommandMethod(@NotNull final Method method) throws InvalidCommandMethodException {
        isBase = isBase(method);
        if (!isBase && !isSub(method) && !isArgument(method)) {
            throw new InvalidCommandMethodException("Command method " + method.getName() + " is not a base or sub command!");
        }

        isConsole = isConsole(method);
        if (!isConsole && !isPlayer(method)) {
            throw new InvalidCommandMethodException("Command method " + method.getName() + " does not specify a sender!");
        }

        this.method = method;
    }

    private boolean isBase(@NotNull final Method method) {
        return method.isAnnotationPresent(CommandBase.class);
    }

    private boolean isSub(@NotNull final Method method) {
        return method.isAnnotationPresent(SubCommand.class);
    }

    private boolean isArgument(@NotNull final Method method) {
        return method.isAnnotationPresent(ArgumentSubCommand.class);
    }

    private boolean isConsole(@NotNull final Method method) {
        return method.isAnnotationPresent(ConsoleCommand.class);
    }

    private boolean isPlayer(@NotNull final Method method) {
        return method.isAnnotationPresent(PlayerCommand.class) && method.getParameters()[0].getType() == Player.class;
    }

}
