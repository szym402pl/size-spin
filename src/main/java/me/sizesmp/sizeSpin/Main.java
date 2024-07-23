package me.sizesmp.sizeSpin;

import me.sizesmp.sizeSpin.Commands.SizeSpinCommand;
import me.sizesmp.sizeSpin.Commands.SizeSpinReloadCommand;
import me.sizesmp.sizeSpin.Listeners.FirstJoinListener;
import me.sizesmp.sizeSpin.Listeners.GUIClickListener;
import me.sizesmp.sizeSpin.Listeners.GUICloseListener;
import me.sizesmp.sizeSpin.Objects.GUI;
import me.sizesmp.sizeSpin.Objects.SizeObject;
import me.sizesmp.sizeSpin.Utils.SizeObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Main extends JavaPlugin {

    public static ArrayList<SizeObject> globalSizeObjectList = new ArrayList<>();
    private SizeObjectManager soManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();

        this.soManager = new SizeObjectManager(this);

        Bukkit.getPluginManager().registerEvents(new GUIClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GUICloseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FirstJoinListener(new SizeSpinCommand
                (new GUI(this, soManager),this)), this);

        getCommand("sizespin").setExecutor(new SizeSpinCommand(new GUI(this, soManager), this));
        getCommand("sizespinreload").setExecutor(new SizeSpinReloadCommand(soManager, this));

        soManager.initializeGlobalSizeObjectList();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPISizeExpansion(this).register();
        } else {
            Bukkit.getLogger().warning("PlaceholderAPI not found! SizeSpin won't have any placeholders.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("no shutdown logic :(");
    }
}
