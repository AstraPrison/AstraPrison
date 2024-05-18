package dev.fabled.astra.utils;

import com.google.gson.*;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MineWriter {

    public static final String FILE = "plugins/Astra/data/mines.json";

    public static void writeMineToFile(@NotNull final Location pos1, @NotNull final Location pos2, @NotNull final String mineName) {
        try {
            JsonObject jsonObject;
            File file = new File(FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();
            } else {
                jsonObject = new JsonObject();
            }

            JsonArray minesArray;
            if (jsonObject.has("mines")) {
                minesArray = jsonObject.getAsJsonArray("mines");
            } else {
                minesArray = new JsonArray();
                jsonObject.add("mines", minesArray);
            }

            JsonObject newMine = new JsonObject();
            newMine.addProperty("name", mineName);

            JsonObject pos1Data = new JsonObject();
            pos1Data.addProperty("startX", pos1.getX());
            pos1Data.addProperty("startY", pos1.getY());
            pos1Data.addProperty("startZ", pos1.getZ());
            newMine.add("pos1", pos1Data);

            JsonObject pos2Data = new JsonObject();
            pos2Data.addProperty("endX", pos2.getX());
            pos2Data.addProperty("endY", pos2.getY());
            pos2Data.addProperty("endZ", pos2.getZ());
            newMine.add("pos2", pos2Data);

            minesArray.add(newMine);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(jsonObject);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonOutput);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
