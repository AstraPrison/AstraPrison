package dev.fabled.astra.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidBlockMaterialException extends IllegalArgumentException {

    public InvalidBlockMaterialException() {
        super();
    }

    public InvalidBlockMaterialException(final @NotNull String message) {
        super(message);
    }

}
