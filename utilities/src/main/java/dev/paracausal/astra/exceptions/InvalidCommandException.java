package dev.paracausal.astra.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException() {}

    public InvalidCommandException(@NotNull final String message) {
        super(message);
    }

}
