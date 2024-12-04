package dev.fabled.astra.locale.impl;

import dev.fabled.astra.locale.annotations.AstraMessageKey;
import org.jetbrains.annotations.NotNull;

public final class SellPriceMessageKeys {

    @AstraMessageKey
    public static final @NotNull String
            HELP,
            INPUT_PRICE,
            INPUT_BLOCK,
            SET,
            PRICE;

    static {
        final String sellPrice = "sell-price.";
        HELP = sellPrice + "help";
        INPUT_PRICE = sellPrice + "input-price";
        INPUT_BLOCK = sellPrice + "input-block";
        SET = sellPrice + "set";
        PRICE = sellPrice + "price";
    }

}
