package me.sizesmp.sizeSpin.Objects;

import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Utils.SizeObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GUI {
    private final Main main;
    private final SizeObjectManager soManager;

    public GUI(Main main, SizeObjectManager soManager) {
        this.main = main;
        this.soManager = soManager;
    }

    public Inventory createBaseGUI(Player player) {

        String guiName = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("gui-name"));
        String guiFillerEtcName = main.getConfig().getString("gui-frame-and-filler-display-name") == null ? " "
                : main.getConfig().getString("gui-frame-and-filler-display-name");

        Material frameMaterial = Material.valueOf(main.getConfig().getString("gui-frame-material"));
        ItemStack frame = new ItemStack(frameMaterial);
        ItemMeta frameMeta = frame.getItemMeta();

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();

        int[] drawItemSlots = new int[]{11,12,13,14,15};
        int[] halfFrameSlots = new int[]{0,1,7,8,9,17,18,19,25,26};
        int[] fullFrameSlots = new int[]{0,1,2,3,5,6,7,8,9,17,18,19,20,21,23,24,25,26};
        String guiFrameStyle = main.getConfig().getString("gui-frame-style").toLowerCase();

        Inventory gui = Bukkit.createInventory(player, 27, guiName);

        frameMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', guiFillerEtcName));
        frame.setItemMeta(frameMeta);
        fillerMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', guiFillerEtcName));
        filler.setItemMeta(fillerMeta);

        ArrayList<ItemStack> itemList = soManager.generateItemPool();

        for (int i = 0; i < drawItemSlots.length; i++) {
            ItemStack randomItem = soManager.returnRandomItem(itemList);
            gui.setItem(drawItemSlots[i], randomItem);
        }

        if (main.getConfig().getBoolean("gui-arrow-indicators")) {
            ItemStack bannerUP = new ItemStack(Material.WHITE_BANNER);
            BannerMeta metaUP = (BannerMeta) bannerUP.getItemMeta();
            String arrowColor = main.getConfig().getString("gui-arrow-indicators-color");
            ArrayList<String> emptyLore = new ArrayList<String>();

            metaUP.addPattern(new Pattern(DyeColor.valueOf(arrowColor), PatternType.STRIPE_CENTER));
            metaUP.addPattern(new Pattern(DyeColor.valueOf(arrowColor), PatternType.STRIPE_TOP));
            metaUP.addPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
            metaUP.setDisplayName(guiFillerEtcName);
            metaUP.setLore(emptyLore);
            metaUP.setHideTooltip(true);
            bannerUP.setItemMeta(metaUP);

            ItemStack bannerDOWN = new ItemStack(Material.WHITE_BANNER);
            BannerMeta metaDOWN = (BannerMeta) bannerDOWN.getItemMeta();

            metaDOWN.addPattern(new Pattern(DyeColor.valueOf(arrowColor), PatternType.STRIPE_CENTER));
            metaDOWN.addPattern(new Pattern(DyeColor.valueOf(arrowColor), PatternType.STRIPE_BOTTOM));
            metaDOWN.addPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
            metaDOWN.setDisplayName(guiFillerEtcName);
            metaDOWN.setLore(emptyLore);
            metaDOWN.setHideTooltip(true);
            bannerDOWN.setItemMeta(metaDOWN);

            gui.setItem(4, bannerDOWN);
            gui.setItem(22, bannerUP);
        }

        switch (guiFrameStyle) {
            case "half":
                for (int i : halfFrameSlots) {
                    gui.setItem(i, frame);
                }
                fillInventory(filler, gui);
                break;
            case "full":
                for (int i : fullFrameSlots) {
                    gui.setItem(i, frame);
                }
                fillInventory(filler, gui);
                break;
            default:
                fillInventory(filler, gui);
        }

        return gui;
    }

    public void fillInventory(ItemStack filler, Inventory inventory) {

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, filler);
            }
        }
    }
}
