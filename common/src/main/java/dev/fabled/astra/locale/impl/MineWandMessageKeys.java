package dev.fabled.astra.locale.impl;

import dev.fabled.astra.locale.annotations.AstraMessageKey;
import org.jetbrains.annotations.NotNull;

public final class MineWandMessageKeys {

    @AstraMessageKey
    public static final @NotNull String
            GIVE_SELF,
            GIVE_OTHER,
            GIVE_RECEIVED,
            CORNER_SET,
            CORNER_MISSING;

    static {
        final String mineWand = "mine-wand.";
        GIVE_SELF = mineWand + "give.self";
        GIVE_OTHER = mineWand + "give.other";
        GIVE_RECEIVED = mineWand + "give.received";

        CORNER_SET = mineWand + "corners.set";
        CORNER_MISSING = mineWand + "corners.missing";
    }

}
