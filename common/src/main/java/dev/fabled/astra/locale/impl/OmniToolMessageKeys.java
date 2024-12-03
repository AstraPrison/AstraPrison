package dev.fabled.astra.locale.impl;

import dev.fabled.astra.locale.annotations.AstraMessageKey;
import org.jetbrains.annotations.NotNull;

public final class OmniToolMessageKeys {

    @AstraMessageKey
    public static final @NotNull String
            HELP,
            GIVE_SELF,
            GIVE_OTHER,
            GIVE_RECEIVED;

    static {
        final String omnitool = "omni-tool.";
        HELP = omnitool + "help";
        GIVE_SELF = omnitool + "give.self";
        GIVE_OTHER = omnitool + "give.other";
        GIVE_RECEIVED = omnitool + "give.received";
    }

}
