package dev.fabled.astra.locale.impl;

import dev.fabled.astra.locale.annotations.AstraMessageKey;
import org.jetbrains.annotations.NotNull;

public final class AdminMessageKeys {

    @AstraMessageKey
    public static final @NotNull String
            ADMIN_HELP,
            ADMIN_RELOAD,
            MENU_OPEN_OTHER;

    static {
        final String admin = "admin.";
        ADMIN_HELP = admin + "help";
        ADMIN_RELOAD = admin + "reload";
        MENU_OPEN_OTHER = admin + "menu-open-other";
    }

}
