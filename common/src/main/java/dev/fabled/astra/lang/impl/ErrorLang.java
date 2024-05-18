package dev.fabled.astra.lang.impl;

import dev.fabled.astra.lang.interfaces.LangKeys;
import dev.fabled.astra.lang.annotations.LangKey;

public class ErrorLang implements LangKeys {

    @LangKey
    public static final String
            NO_PERMISSION = "errors.no-permission",
            SELECT_PLAYER = "errors.select-player",
            INVALID_PLAYER = "errors.invalid-player";

}
