package me.sizesmp.sizeSpin.Listeners;

import me.sizesmp.sizeSpin.Utils.GUIAnimator;
import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Commands.SizeSpinCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUICloseListener implements Listener {

    Main main;
    public GUICloseListener(Main main){
        this.main = main;
    }


    @EventHandler
    public void onGUIClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        GUIAnimator animator = SizeSpinCommand.getAnimatorForPlayer(player);
        if (animator != null && event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&',
                main.getConfig().getString("gui-name")))) {
            animator.stopAnimation(player);
        }
    }

}
