package dev.fabled.astra.mines;

import dev.fabled.astra.utils.configuration.JsonConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MineManager {

    private final JsonConfig mineData;
    private final Map<String, Mine> mines;

    public MineManager() {
        mineData = new JsonConfig("data/mines.json");
        mines = new HashMap<>();
    }

    public void onReload() {
        final Set<String> mineSet = mineData.options().getSet("mines");
        mineSet.forEach(id -> mines.put(id, new Mine(id)));
    }

}
