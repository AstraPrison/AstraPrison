package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.BrigadierCommand;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.OmniToolCommand;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.OmniToolLang;
import dev.fabled.astra.lang.interfaces.LangKeys;
import dev.fabled.astra.modules.AstraModule;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OmniToolModule extends AstraModule {

    public static final String ID = "omnitool";
    private static OmniToolModule instance;

    public static @NotNull OmniToolModule getInstance() {
        if (instance == null) {
            final AstraModule module = Astra.getUtilities().getModuleManager().getModule(ID);
            instance = module == null
                    ? new OmniToolModule()
                    : (OmniToolModule) module;
        }

        return instance;
    }

    private NamespacedKey omniToolKey;

    private final List<LangKeys> lang = List.of(
            new OmniToolLang()
    );

    private final List<BrigadierCommand> commands = List.of(
            new OmniToolCommand()
    );

    public OmniToolModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        lang.forEach(LocaleManager.getInstance()::registerLanguageKeys);
    }

    @Override
    public void onEnable() {
        omniToolKey = new NamespacedKey(Astra.getPlugin(), "astra_omnitool");

        final CommandManager commandManager = Astra.getUtilities().getCommandManager();
        commands.forEach(commandManager::register);
    }

    public NamespacedKey getOmniToolKey() {
        return omniToolKey;
    }

}
