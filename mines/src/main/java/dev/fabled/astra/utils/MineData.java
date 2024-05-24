package dev.fabled.astra.utils;

import org.bukkit.Material;

public class MineData {
    private final int startX;
    private final int startY;
    private final int startZ;
    private final int endX;
    private final int endY;
    private final int endZ;
    private final Material material;

    private final Material material2;
    private final Material material3;

    private final String resetType;

    private final Boolean airgap;

    public MineData(int startX, int startY, int startZ, int endX, int endY, int endZ, Material material, Material material2, Material material3, String resetType, Boolean airgap) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
        this.material = material;
        this.material2 = material2;
        this.material3 = material3;
        this.resetType = resetType;
        this.airgap = airgap;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndZ() {
        return endZ;
    }

    public Material getMaterial() {
        return material;
    }

    public Material getMaterial2() {
        return material2;
    }

    public Material getMaterial3() {
        return material3;
    }

    public String resetType() {
        return resetType;
    }

    public Boolean airgap() {
        return airgap;
    }
}
