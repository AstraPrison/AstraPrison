package dev.fabled.astra.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class LuckyBlockReward {

    private static final String REWARDS_FILE_PATH = "plugins/Astra/data/luckyblock_rewards.json";

    public static void giveRandomLuckyBlockReward(Player player) {
        List<ItemStack> rewards = loadLuckyBlockRewards();
        if (rewards != null && !rewards.isEmpty()) {
            Random random = new Random();
            if (random.nextBoolean()) {
                player.sendMessage(MiniColor.parse("<yellow>LuckyBlock: </yellow>" + "you received nothing this time."));
                return;
            }
            ItemStack reward = rewards.get(random.nextInt(rewards.size()));
            player.getInventory().addItem(reward);
            player.sendMessage(MiniColor.parse("<yellow>LuckyBlock: </yellow>" + "<green>You received </green>" + reward.getAmount() + " " + reward.getType().name() + "!"));
        } else {
            player.sendMessage("No rewards available.");
        }
    }

    private static List<ItemStack> loadLuckyBlockRewards() {
        try (FileReader reader = new FileReader(REWARDS_FILE_PATH)) {
            Type listType = new TypeToken<List<ItemStack>>() {
            }.getType();
            return new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

