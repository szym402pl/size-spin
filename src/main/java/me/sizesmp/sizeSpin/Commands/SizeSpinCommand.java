package me.sizesmp.sizeSpin.Commands;

import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Objects.GUI;
import me.sizesmp.sizeSpin.Utils.GUIAnimator;
import me.sizesmp.sizeSpin.Utils.SizeObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SizeSpinCommand implements CommandExecutor {
    GUI guiClass;
    private Main main;
    private static final Map<UUID, GUIAnimator> playerAnimators = new HashMap<>();
    public static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 2 * 60 * 60 * 1000;

    public SizeSpinCommand(GUI guiClass, Main main) {
        this.guiClass = guiClass;
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player player) {

            if (args.length == 0) {

                handleAnimationStart(player);

            } else if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (isInCooldown(target)) {

                            long timeLeft = (cooldowns.get(target.getUniqueId()) + COOLDOWN_TIME - System.currentTimeMillis()) / 1000;
                            player.sendMessage(ChatColor.RED + "You cannot apply this command on " + target.getName() + " for another " + timeLeft / 60 + " minutes.");

                        } else {

                            handleAnimationStart(target);
                            cooldowns.put(target.getUniqueId(), System.currentTimeMillis());

                            player.sendMessage(ChatColor.GREEN + "Successfully used Size Spin on " + target.getName());
                        }

                    } else {
                        player.sendMessage(ChatColor.RED + "Specified player isn't online!");
                    }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /sizespin <player>");
            }

        } else {
            if (args.length != 1) {
                Bukkit.getLogger().info("Usage: sizespin <player>");
            } else {
                if (Bukkit.getPlayer(args[0]) != null) {

                    Player target = Bukkit.getPlayer(args[0]);
                    handleAnimationStart(target);
                    Bukkit.getLogger().info("Successfully used Size Spin on " + target.getName());

                } else {
                    Bukkit.getLogger().info("Specified player isn't online!");
                }
            }
        }

        return true;
    }

    private boolean isInCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long lastUsed = cooldowns.get(playerId);
            return System.currentTimeMillis() - lastUsed < COOLDOWN_TIME;
        }
        return false;
    }

    public static GUIAnimator getAnimatorForPlayer(Player player) {
        return playerAnimators.get(player.getUniqueId());
    }

    public void handleAnimationStart(Player player) {
        Inventory gui = guiClass.createBaseGUI(player);
        player.openInventory(gui);

        if (playerAnimators.containsKey(player.getUniqueId())) {
            playerAnimators.get(player.getUniqueId()).stopAnimation(player);
        }

        GUIAnimator animator = new GUIAnimator(main, new SizeObjectManager(main));
        playerAnimators.put(player.getUniqueId(), animator);
        animator.startAnimation(player, gui);
    }
}
