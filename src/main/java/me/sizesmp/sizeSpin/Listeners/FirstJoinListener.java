package me.sizesmp.sizeSpin.Listeners;

import me.sizesmp.sizeSpin.Commands.SizeSpinCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstJoinListener implements Listener {

    SizeSpinCommand sizeSpinCommand;

    public FirstJoinListener(SizeSpinCommand sizeSpinCommand) {
        this.sizeSpinCommand = sizeSpinCommand;
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {

        if (!event.getPlayer().hasPlayedBefore()) {
            sizeSpinCommand.handleAnimationStart(event.getPlayer());
        }

    }

}
