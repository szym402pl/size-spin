package me.sizesmp.sizeSpin.Utils;

import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Objects.SizeObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SizeObjectManager {

    private Main main;

    public SizeObjectManager(Main instance) {
        this.main = instance;
    }

    public SizeObject getSizeObjectByItemName(String name) {
        for (SizeObject obj : Main.globalSizeObjectList) {
            if (obj.getGuiItem().getItemMeta().getDisplayName().equals
                    (ChatColor.translateAlternateColorCodes('&', name))) {
                return obj;
            }
        }
        return null;
    }

    public void enhancePlayerBasedOnSizeObject(Player player, SizeObject obj) {

        double genMovementSpeedDefault = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getDefaultValue();
        double genScaleDefault = player.getAttribute(Attribute.GENERIC_SCALE).getDefaultValue();
        double genJumpStrengthDefault = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).getDefaultValue();
        double genSafeFallDistDefault = player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).getDefaultValue();
        double blockInterRangeDefault = player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).getDefaultValue();
        double entInterRangeDefault = player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getDefaultValue();

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue
                (genMovementSpeedDefault/2 * obj.getSpeed());
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue
                ((genScaleDefault/2) * obj.getHeight());
        player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue
                (genJumpStrengthDefault/2 * obj.getJumpStrength());
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(obj.getHealthPoints());

        // safe fall distance compensation
        if (obj.getHeight() > 1.0) {
            player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue
                    (genSafeFallDistDefault * obj.getJumpStrength());
        }

        // interaction range compensation
        if (obj.getHeight() > 1.0) {
            player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).setBaseValue
                    (entInterRangeDefault/2 * obj.getHeight());

            player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).setBaseValue
                    (blockInterRangeDefault/2 * obj.getHeight());
        }

    }

    public void initializeGlobalSizeObjectList() {
        Main.globalSizeObjectList.clear();

        if (!main.getConfig().isConfigurationSection("size-presets")) {
            System.out.println("No size-presets section found in the config.");
            return;
        }

        for (String key : main.getConfig().getConfigurationSection("size-presets").getKeys(false)) {
            double height = main.getConfig().getDouble("size-presets." + key + ".character-height-in-blocks");
            double chance = main.getConfig().getDouble("size-presets." + key + ".chance-in-percent");
            double jump = main.getConfig().getDouble("size-presets." + key + ".jump-height-multiplier");
            double speed = main.getConfig().getDouble("size-presets." + key + ".speed-multiplier");
            double hp = main.getConfig().getDouble("size-presets." + key + ".health-points");

            String materialName = main.getConfig().getString("size-presets." + key + ".gui-item.material");
            if (materialName == null) {
                System.out.println("No material specified for size-preset " + key);
                continue;
            }

            Material material;
            try {
                material = Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid material specified for size-preset " + key + ": " + materialName);
                continue;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            boolean enchanted = main.getConfig().getBoolean("size-presets." + key + ".gui-item.enchanted");
            String itemName = main.getConfig().getString("size-presets." + key + ".gui-item.item-name");

            meta.setEnchantmentGlintOverride(enchanted);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            meta.setLore(new ArrayList<String>());
            item.setItemMeta(meta);

            SizeObject object = new SizeObject(chance, height, jump, hp, speed, item);
            Main.globalSizeObjectList.add(object);
        }
    }

    public int getSameChanceCount(double chance) {
        int count = 0;

        for (String size : main.getConfig().getConfigurationSection("size-presets").getKeys(false)) {
            double chance1 = main.getConfig().getDouble("size-presets." + size + ".chance-in-percent");

            if (chance1 == chance) { count++; }
        }

        return count;
    }

    public ArrayList<ItemStack> generateItemPool(){
        ArrayList<ItemStack> itemList = new ArrayList<>();
        Map<ItemStack, Double> map = getItemsAndTheirChancesFromConfig();

        for (Map.Entry<ItemStack, Double> entry : map.entrySet()) {
            double processedChance = entry.getValue()/getSameChanceCount(entry.getValue());
            int roundedChance = returnRoundedAndPositive(processedChance);

            for (int i = 0; i < roundedChance; i++) {
                itemList.add(entry.getKey());
            }
        }

        return itemList;
    }

    public ItemStack returnRandomItem(ArrayList<ItemStack> list) {
        Collections.shuffle(list);
        return list.get(list.size() -1);
    }

    public Map<ItemStack, Double> getItemsAndTheirChancesFromConfig() {
        Map<ItemStack, Double> map = new HashMap<>();

        for (SizeObject obj : Main.globalSizeObjectList) {
            map.put(obj.getGuiItem(), obj.getChance());
        }

        return map;
    }

    public int returnRoundedAndPositive(double value) {
        int rounded = (int) Math.round(value);

        return Math.max(rounded, 1);
    }
}
