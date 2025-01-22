package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.BrigadierCommand;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.FillRegionCommand;
import dev.fabled.astra.commands.MineWandCommand;
import dev.fabled.astra.listeners.AstraListener;
import dev.fabled.astra.listeners.MineWandListener;
import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.mines.wand.MineWandSelection;
import dev.fabled.astra.modules.AstraModule;
import dev.fabled.astra.utils.configuration.JsonConfig;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MinesModule extends AstraModule {

    public static final @NotNull String ID;

    static {
        ID = "mines";
    }

    private static MinesModule instance;

    public static MinesModule getInstance() {
        if (instance == null) {
            instance = new MinesModule();
        }

        return instance;
    }

    private JsonConfig clipboardJson;
    private MineWandSelection mineWandSelection;

    private MinesModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        MineWand.getInstance();
        clipboardJson = new JsonConfig("modules/mines/data/clipboard.json");
        mineWandSelection = new MineWandSelection();
    }

    @Override
    public void onEnable() {
        final List<AstraListener> listeners = List.of(
                new MineWandListener()
        );

        listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Astra.getPlugin()));

        final List<BrigadierCommand> commands = List.of(
                new MineWandCommand(),
                new FillRegionCommand()
        );

        final CommandManager commandManager = CommandManager.getInstance();
        commands.forEach(commandManager::register);
    }

    @Override
    public void onDisable() {
        mineWandSelection.saveAll();
    }

    @Override
    public void onReload() {

    }

    public JsonConfig getClipboardJson() {
        return clipboardJson;
    }

    public MineWandSelection getMineWandSelection() {
        return mineWandSelection;
    }

}
