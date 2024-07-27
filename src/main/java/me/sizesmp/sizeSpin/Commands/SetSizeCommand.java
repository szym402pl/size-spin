package me.sizesmp.sizeSpin.Commands;

import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Objects.SizeObject;
import me.sizesmp.sizeSpin.Utils.SizeObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class SetSizeCommand implements CommandExecutor {

    Main main;
    SizeObjectManager sizeObjectManager;

    public SetSizeCommand(Main main, SizeObjectManager sizeObjectManager) {
        this.main = main;
        this.sizeObjectManager = sizeObjectManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setsize <size> <player>");
            return true;
        }

        ArrayList<String> playerNames = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));

        HashMap<String, SizeObject> sizeMap = new HashMap<>();
        Main.globalSizeObjectList.forEach(obj -> sizeMap.put(String.valueOf(obj.getHeight()), obj));

        if (!sizeMap.containsKey(args[0])) {
            sender.sendMessage(ChatColor.RED + "Specified size " + args[0] + " is invalid!");
        } else if (!playerNames.contains(args[1])) {
            sender.sendMessage(ChatColor.RED + "Player named " + args[1] + " is not online!");;
        } else {
            Player target = Bukkit.getPlayer(args[1]);

            if (main.getConfig().getBoolean("title-on-size-change")) {
                String titleMessage = ChatColor.translateAlternateColorCodes
                        ('&', main.getConfig().getString("title-message"));

                target.sendTitle("", titleMessage, 10, 15, 10);
            }

            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.1f, 1.1f);
            String notification = ChatColor.translateAlternateColorCodes
                            ('&', main.getConfig().getString("notification-message"))
                    .replace("{item-name}", sizeMap.get(args[1]).getGuiItem().getItemMeta().getDisplayName());
            target.sendMessage(notification);

            sizeObjectManager.enhancePlayerBasedOnSizeObject(target, sizeMap.get(args[0]));

            sender.sendMessage(ChatColor.GREEN + "Successfully set the size of " + args[1] + " to " + args[0]);
        }
        return true;
    }
}
