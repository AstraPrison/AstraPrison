package dev.fabled.astra.utils.tools;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public final class ToolSpeed {

    public static int getToolMultiplier(final @NotNull ItemStack itemStack) {
        final String material = itemStack.getType().name().toLowerCase();
        if (!material.contains("_")) {
            return 0;
        }

        final String type = material.split("_")[0];
        return switch (type) {
            case "wooden" -> 2;
            case "stone" -> 4;
            case "iron" -> 6;
            case "diamond" -> 8;
            case "netherite" -> 9;
            case "golden" -> 12;
            default -> 0;
        };
    }

    public static double calculate(final @NotNull Player player, final @NotNull ItemStack itemStack, final @NotNull BlockState blockState) {
        final boolean isPreferredTool = ToolType.getToolType(itemStack).isPreferredTool(blockState);
        final boolean canHarvest = !blockState.getDrops(itemStack).isEmpty();
        final int toolMultiplier = getToolMultiplier(itemStack);

        double speedMultiplier = 1;
        if (isPreferredTool && canHarvest) {
            speedMultiplier = toolMultiplier;
        }

        final int efficiencyLevel = itemStack.getEnchantmentLevel(Enchantment.EFFICIENCY);
        if (efficiencyLevel > 0 && isPreferredTool) {
            speedMultiplier += Math.pow(efficiencyLevel, 2) + 1;
        }

        if (player.hasPotionEffect(PotionEffectType.HASTE) || player.hasPotionEffect(PotionEffectType.CONDUIT_POWER)) {
            final PotionEffect haste = player.getPotionEffect(PotionEffectType.HASTE);
            final PotionEffect conduit = player.getPotionEffect(PotionEffectType.CONDUIT_POWER);
            int hasteLevel = haste == null ? 0 : haste.getAmplifier() + 1;
            Bukkit.getConsoleSender().sendMessage("Haste: " + hasteLevel);
            int conduitLevel = conduit == null ? 0 : conduit.getAmplifier() + 1;

            speedMultiplier *= 0.2 * Math.max(hasteLevel, conduitLevel) + 1;
        }

        if (player.hasPotionEffect(PotionEffectType.MINING_FATIGUE)) {
            final PotionEffect fatigue = player.getPotionEffect(PotionEffectType.MINING_FATIGUE);
            int fatigueLevel = fatigue == null ? 0 : fatigue.getAmplifier() + 1;
            speedMultiplier *= Math.pow(0.3, Math.min(fatigueLevel, 4));
        }

        final ItemStack helmet = player.getInventory().getHelmet();
        if (player.isInWater() && (helmet == null || !helmet.containsEnchantment(Enchantment.AQUA_AFFINITY))) {
            speedMultiplier *= 0.2;
        }

        if (player.isFlying()) {
            speedMultiplier *= 0.2;
        }

        float hardness = blockState.getType().getHardness();
        double damage = speedMultiplier / hardness;

        if (canHarvest) {
            damage *= 0.03333333333;
        }
        else {
            damage *= 0.01;
        }

        // Instant breaking
        if (damage > 1) {
            return 0f;
        }

        return Math.ceil(1 / damage);
    }

}
