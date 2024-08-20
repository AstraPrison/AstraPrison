package dev.fabled.astra.exceptions;

import org.jetbrains.annotations.NotNull;

public final class InvalidMaterialException extends Exception {

    public InvalidMaterialException() {
        super();
    }

    public InvalidMaterialException(@NotNull final String message) {
        super(message);
    }

}
