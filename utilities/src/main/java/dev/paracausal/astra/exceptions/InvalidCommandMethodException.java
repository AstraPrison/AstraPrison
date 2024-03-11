package dev.paracausal.astra.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidCommandMethodException extends RuntimeException {

    public InvalidCommandMethodException() {}

    public InvalidCommandMethodException(@NotNull final String message) {
        super(message);
    }

}
