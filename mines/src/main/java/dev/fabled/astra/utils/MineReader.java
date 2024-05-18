package dev.fabled.astra.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;

public class MineReader {

    public static MineData readMineData(String filePath) {
        Gson gson = new Gson();
        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray minesArray = jsonObject.getAsJsonArray("mines");
            for (int i = 0; i < minesArray.size(); i++) {
                JsonObject mine = minesArray.get(i).getAsJsonObject();
                String mineName = mine.get("name").getAsString();
                JsonObject pos1 = mine.getAsJsonObject("pos1");
                JsonObject pos2 = mine.getAsJsonObject("pos2");

                int startX = pos1.get("startX").getAsInt();
                int startY = pos1.get("startY").getAsInt();
                int startZ = pos1.get("startZ").getAsInt();
                int endX = pos2.get("endX").getAsInt();
                int endY = pos2.get("endY").getAsInt();
                int endZ = pos2.get("endZ").getAsInt();
                return new MineData(startX, startY, startZ, endX, endY, endZ);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
