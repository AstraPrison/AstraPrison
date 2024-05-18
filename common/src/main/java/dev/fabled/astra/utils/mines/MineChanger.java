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

    public static boolean updateMineMaterialInJson(String mineName, Material newMaterial) {
        File file = new File("plugins/Astra/data/mines.json");
        if (!file.exists()) return false;

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject.has("mines")) {
                JsonArray minesArray = jsonObject.getAsJsonArray("mines");
                for (JsonElement element : minesArray) {
                    JsonObject mine = element.getAsJsonObject();
                    if (mine.has("name") && mine.get("name").getAsString().equals(mineName)) {
                        mine.add("material", new JsonPrimitive(newMaterial.name()));
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

}
