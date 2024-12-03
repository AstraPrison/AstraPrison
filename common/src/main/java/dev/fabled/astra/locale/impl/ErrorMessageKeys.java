package dev.fabled.astra.locale.impl;

import dev.fabled.astra.locale.annotations.AstraMessageKey;
import org.jetbrains.annotations.NotNull;

public final class ErrorMessageKeys {

    @AstraMessageKey
    public static final @NotNull String
            NO_PERMISSION,
            SELECT_PLAYER,
            INVALID_PLAYER,
            SELECT_MENU,
            INVALID_MENU;

    static {
        final String error = "error.";
        NO_PERMISSION = error + "no-permission";
        SELECT_PLAYER = error + "select-player";
        INVALID_PLAYER = error + "invalid-player";
        SELECT_MENU = error + "select-menu";
        INVALID_MENU = error + "invalid-menu";
    }

}
