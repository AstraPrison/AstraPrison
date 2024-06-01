package dev.fabled.astra.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineReader {

    private static final Map<String, MineData> mineMap = new HashMap<>();

    public static MineData readMineData(String filePath, String mineName) {
        File file = new File(filePath);
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonObject mine = minesArray.get(i).getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
                        JsonObject pos1 = mine.getAsJsonObject("pos1");
                        JsonObject pos2 = mine.getAsJsonObject("pos2");

                        int startX = pos1.get("startX").getAsInt();
                        int startY = pos1.get("startY").getAsInt();
                        int startZ = pos1.get("startZ").getAsInt();

                        int endX = pos2.get("endX").getAsInt();
                        int endY = pos2.get("endY").getAsInt();
                        int endZ = pos2.get("endZ").getAsInt();

                        String materialString = mine.get("material1").getAsString();
                        String materialString2 = mine.get("material2").getAsString();
                        String materialString3 = mine.get("material3").getAsString();
                        Material material = parseMaterial(materialString);
                        Material material2 = parseMaterial(materialString2);
                        Material material3 = parseMaterial(materialString3);
                        String resetType = mine.get("resetType").getAsString();
                        Boolean airgap = mine.get("airgap").getAsBoolean();
                        Boolean luckyblocks = mine.get("luckyblocks").getAsBoolean();
                        Material luckyblockMaterial = Material.valueOf(mine.get("luckyblockMaterial").getAsString());
                        if (material != null && material2 != null && material3 != null) {
                            mineMap.put(mineName, new MineData(startX, startY, startZ, endX, endY, endZ, material, material2, material3, resetType, airgap, luckyblocks, luckyblockMaterial));
                            return new MineData(startX, startY, startZ, endX, endY, endZ, material, material2, material3, resetType, airgap, luckyblocks, luckyblockMaterial);
                        } else {
                            System.err.println("Invalid material name: " + materialString);
                            return null;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getMineNames(String filePath) {
        List<String> mineNames = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return mineNames;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonObject mine = minesArray.get(i).getAsJsonObject();
                    if (mine.has("name")) {
                        mineNames.add(mine.get("name").getAsString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mineNames;
    }

    private static Material parseMaterial(String materialString) {
        try {
            return Material.valueOf(materialString);
        } catch (IllegalArgumentException e) {
            // Material not found
            return null;
        }
}


    public static Map<String, MineData> getMineMap() {
        return mineMap;
    }
}
