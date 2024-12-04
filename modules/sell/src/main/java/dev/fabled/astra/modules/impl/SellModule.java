package dev.fabled.astra.modules.impl;

import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.SellPriceCommand;
import dev.fabled.astra.modules.AstraModule;
import dev.fabled.astra.sell.SellPriceManager;
import org.jetbrains.annotations.NotNull;

public class SellModule extends AstraModule {

    public static final @NotNull String ID;

    static {
        ID = "sell";
    }

    public SellModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        SellPriceManager.getInstance();
    }

    @Override
    public void onEnable() {
        final CommandManager commandManager = CommandManager.getInstance();
        commandManager.register(new SellPriceCommand());
    }

    @Override
    public void onDisable() {
        SellPriceManager.getInstance().onDisable();
    }

    @Override
    public void onReload() {

    }

}
