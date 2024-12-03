package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.OmniToolCommand;
import dev.fabled.astra.listeners.AstraListener;
import dev.fabled.astra.listeners.OmniToolJoinListener;
import dev.fabled.astra.listeners.OmniToolLockListener;
import dev.fabled.astra.modules.AstraModule;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.omnitool.levels.OmniToolLevelManager;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class OmniToolModule extends AstraModule {

    public static final @NotNull String ID;

    static {
        ID = "omnitool";
    }

    public static OmniToolModule instance;

    public static OmniToolModule getInstance() {
        if (instance == null) {
            instance = new OmniToolModule();
        }

        return instance;
    }

    private YamlConfig omniToolYml;
    private YamlConfig levelsYml;
    private OmniToolLevelManager levelManager;

    private final @NotNull List<AstraListener> listeners = List.of(
            new OmniToolJoinListener(),
            new OmniToolLockListener()
    );

    private OmniToolModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        omniToolYml = new YamlConfig("modules/omnitool/omnitool.yml");
        levelsYml = new YamlConfig("modules/omnitool/levels.yml");
        levelManager = new OmniToolLevelManager();
        OmniTool.onLoad();
    }

    @Override
    public void onEnable() {
        listeners.forEach(listener -> {
            listener.onReload();
            Bukkit.getPluginManager().registerEvents(listener, Astra.getPlugin());
        });

        CommandManager.getInstance().register(new OmniToolCommand());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        omniToolYml.save();
        omniToolYml.reload();
        levelsYml.save();
        levelsYml.reload();
        OmniTool.onReload();
        levelManager.onReload();

        listeners.forEach(AstraListener::onReload);
    }

    public YamlConfig getOmniToolYml() {
        return omniToolYml;
    }

    public YamlConfig getLevelsYml() {
        return levelsYml;
    }

    public OmniToolLevelManager getLevelManager() {
        return levelManager;
    }

}
