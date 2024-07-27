package me.sizesmp.sizeSpin.Commands;

import me.sizesmp.sizeSpin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetSizeTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        if (args.length == 1) {
            ArrayList<String> sizes = new ArrayList<>();
            Main.globalSizeObjectList.forEach(obj -> sizes.add(String.valueOf(obj.getHeight())));

            return StringUtil.copyPartialMatches(args[0], sizes, new ArrayList<>());
        } else if (args.length == 2) {
            ArrayList<String> playerNames = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));

            return StringUtil.copyPartialMatches(args[1], playerNames, new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
