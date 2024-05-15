package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.modules.AstraModule;
import org.bukkit.NamespacedKey;

public class OmniToolModule extends AstraModule {

    public static final String ID;

    static {
        ID = "omnitool";
    }

    public static OmniToolModule getInstance() {
        final AstraModule module = Astra.getUtilities().getModuleManager().getModule(ID);
        if (module == null) {
            return null;
        }

        return (OmniToolModule) module;
    }

    private NamespacedKey omniToolKey;

    public OmniToolModule() {
        super(ID);
    }

    @Override
    public void onEnable() {
        omniToolKey = new NamespacedKey(Astra.getPlugin(), "astra_omnitool");
    }

    public NamespacedKey getOmniToolKey() {
        return omniToolKey;
    }

}
