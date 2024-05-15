package dev.fabled.astra.lang.impl;

import dev.fabled.astra.lang.interfaces.MessageKeys;
import dev.fabled.astra.lang.annotations.MessageKey;

public class ErrorLang implements MessageKeys {

    @MessageKey
    public static final String
            NO_PERMISSION = "errors.no-permission",
            SELECT_PLAYER = "errors.select-player",
            INVALID_PLAYER = "errors.invalid-player";

}
