package me.sizesmp.sizeSpin.Commands;

import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Utils.SizeObjectManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SizeSpinReloadCommand implements CommandExecutor {
    private SizeObjectManager soManager;
    private Main main;
    public SizeSpinReloadCommand(SizeObjectManager soManager, Main main) {
        this.soManager = soManager;
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        try {

            main.reloadConfig();
            soManager.initializeGlobalSizeObjectList();
            sender.sendMessage(ChatColor.GREEN + "[SizeSpin] Config has been reloaded successfully.");

        } catch (Exception exception) {

            sender.sendMessage(ChatColor.RED + "[SizeSpin] Something went wrong while reloading the config! \nCheck the console for more info.");
            exception.printStackTrace();

        }

        return true;
    }
}
