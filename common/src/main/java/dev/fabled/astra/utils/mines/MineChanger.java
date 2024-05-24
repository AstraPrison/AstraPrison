package dev.fabled.astra.utils.mines;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MineChanger {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final Map<Player, String> renameMineMap = new HashMap<>();

    public static boolean updateMineMaterialInJson(String mineName, Material newMaterial, MaterialType materialType) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return false;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (JsonElement element : minesArray) {
                    JsonObject mine = element.getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
                        switch (materialType) {
                            case MATERIAL1:
                                mine.add("material1", new JsonPrimitive(newMaterial.name()));
                                break;
                            case MATERIAL2:
                                mine.add("material2", new JsonPrimitive(newMaterial.name()));
                                break;
                            case MATERIAL3:
                                mine.add("material3", new JsonPrimitive(newMaterial.name()));
                                break;
                            default:
                                return false; // Unknown material type
                        }
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String jsonString = gson.toJson(jsonObject);
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(jsonString);
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMineNameInJson(String oldName, String newName) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return false;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (int i = 0; i < minesArray.size(); i++) {
                    JsonObject mine = minesArray.get(i).getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(oldName)) {
                        mine.add("name", new JsonPrimitive(newName));
                        try (FileWriter writer = new FileWriter(file)) {
                            gson.toJson(jsonObject, writer);
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean toggleResetType(String mineName) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return false;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (JsonElement element : minesArray) {
                    JsonObject mine = element.getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
                        if (mine.has("resetType")) {
                            String currentResetType = mine.get("resetType").getAsString();
                            String newResetType = getNextResetType(currentResetType);
                            mine.add("resetType", new JsonPrimitive(newResetType));
                            try (FileWriter writer = new FileWriter(file)) {
                                gson.toJson(jsonObject, writer);
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean toggleairgap(String mineName) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return false;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (JsonElement element : minesArray) {
                    JsonObject mine = element.getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
                        if (mine.has("airgap")) {
                            boolean currentAirgap = mine.get("airgap").getAsBoolean();
                            boolean newAirgap = !currentAirgap; // Toggle the boolean value
                            mine.add("airgap", new JsonPrimitive(newAirgap));
                            try (FileWriter writer = new FileWriter(file)) {
                                gson.toJson(jsonObject, writer);
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getNextResetType(String currentResetType) {
        switch (currentResetType) {
            case "Blocks":
                return "Periodic";
            case "Periodic":
                return "Timed";
            case "Timed":
                return "Blocks";
            default:
                return "Blocks";
        }
    }

    public enum MaterialType {
        MATERIAL1,
        MATERIAL2,
        MATERIAL3
    }
}
