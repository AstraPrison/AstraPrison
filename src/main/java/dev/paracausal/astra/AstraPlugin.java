package dev.paracausal.astra;

import org.bukkit.plugin.java.JavaPlugin;

public class AstraPlugin extends JavaPlugin implements AstraUtility {
    
    @Override
    public void onLoad() {
        Astra.onLoad(this);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
