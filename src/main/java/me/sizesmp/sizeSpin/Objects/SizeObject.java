package me.sizesmp.sizeSpin.Objects;

import org.bukkit.inventory.ItemStack;

public class SizeObject {

    private double chance;
    private double height;
    private double jumpStrength;
    private double healthPoints;
    private double speed;
    private ItemStack guiItem;

    public SizeObject(double chance, double height, double jump, double hp, double speed, ItemStack item){
        this.speed = speed;
        this.chance = chance;
        this.height = height;
        this.jumpStrength = jump;
        this.healthPoints = hp;
        this.guiItem = item;
    }

    public double getChance() {
        return chance;
    }
    public double getHealthPoints() {
        return healthPoints;
    }
    public double getHeight() {
        return height;
    }
    public double getJumpStrength() {
        return jumpStrength;
    }
    public double getSpeed() { return speed; }
    public ItemStack getGuiItem() {
        return guiItem;
    }
}