package dev.fabled.astra.mines;

import dev.fabled.astra.utils.configuration.JsonConfig;

import java.util.HashMap;
import java.util.Map;

public class MineManager {

    private final JsonConfig mineData;
    private final Map<String, Mine> mines;

    public MineManager() {
        mineData = new JsonConfig("data/mines.json");
        mines = new HashMap<>();
    }


}
