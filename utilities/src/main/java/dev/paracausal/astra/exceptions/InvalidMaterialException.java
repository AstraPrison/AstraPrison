package dev.paracausal.astra.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidMaterialException extends RuntimeException {

    public InvalidMaterialException() {}

    public InvalidMaterialException(@NotNull final String message) {
        super(message);
    }

}
