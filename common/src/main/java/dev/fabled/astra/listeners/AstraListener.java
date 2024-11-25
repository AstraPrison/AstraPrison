package dev.fabled.astra.listeners;

import org.bukkit.event.Listener;

public interface AstraListener extends Listener {

    default void onReload() {}

}
