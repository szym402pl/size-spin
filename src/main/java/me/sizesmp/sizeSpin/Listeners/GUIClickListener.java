package me.sizesmp.sizeSpin.Listeners;

import me.sizesmp.sizeSpin.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIClickListener implements Listener {
    Main main;
    public GUIClickListener(Main main){
        this.main = main;
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        String configTitle = ChatColor.translateAlternateColorCodes('&',
                main.getConfig().getString("gui-name"));

        if (event.getView().getTitle().equals(configTitle)) {
            event.setCancelled(true);
        }
    }
}
