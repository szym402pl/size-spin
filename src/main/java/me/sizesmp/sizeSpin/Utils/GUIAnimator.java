package me.sizesmp.sizeSpin.Utils;

import me.sizesmp.sizeSpin.Main;
import me.sizesmp.sizeSpin.Objects.SizeObject;
import me.sizesmp.sizeSpin.Commands.SizeSpinCommand;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class GUIAnimator {
    private static final int INITIAL_DELAY = 2;
    private static final int MAX_DELAY = 30;
    private static final int MAX_FRAMES = 21;
    private static final int PAUSE_DURATION = 40;
    private final Main main;
    private SizeObjectManager soManager;
    private final Map<Player, AnimationTask> activeAnimations = new HashMap<>();

    public GUIAnimator(Main main, SizeObjectManager soManager) {
        this.main = main;
        this.soManager = soManager;
    }

    public void startAnimation(Player player, Inventory inv) {
        AnimationTask task = new AnimationTask(player, inv, INITIAL_DELAY, 0);
        activeAnimations.put(player, task);
        task.runTask(main);
    }

    public void stopAnimation(Player player) {
        AnimationTask task = activeAnimations.remove(player);
        if (task != null) {
            task.cancel();
        }
    }

    private class AnimationTask extends BukkitRunnable {
        private final Player player;
        private final Inventory inv;
        private final int initialDelay;
        private final int frame;
        private final int delay;

        public AnimationTask(Player player, Inventory inv, int delay, int frame) {
            this.player = player;
            this.inv = inv;
            this.delay = delay;
            this.frame = frame;
            this.initialDelay = delay;
        }

        @Override
        public void run() {
            if (!player.getOpenInventory().getTopInventory().equals(inv)) {
                player.sendMessage(ChatColor.RED + "Size spin has been cancelled!");
                stopAnimation(player);
                SizeSpinCommand.cooldowns.remove(player.getUniqueId());
                return;
            }

            animateFrame(inv);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

            if (frame >= MAX_FRAMES) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.closeInventory();

                        SizeObject drawnSize = soManager.getSizeObjectByItemName
                                (inv.getItem(13).getItemMeta().getDisplayName());

                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.1f, 1.1f);

                        String notification = ChatColor.translateAlternateColorCodes
                                ('&', main.getConfig().getString("notification-message"))
                                .replace("{item-name}", drawnSize.getGuiItem().getItemMeta().getDisplayName());
                        player.sendMessage(notification);

                        if (main.getConfig().getBoolean("title-on-size-change")) {
                            String titleMessage = ChatColor.translateAlternateColorCodes
                                    ('&', main.getConfig().getString("title-message"));

                            player.sendTitle("", titleMessage, 10, 15, 10);
                        }

                        double fireworksIfChanceUnder = main.getConfig().getDouble("fireworks-if-chance-under");
                        boolean fireworks = drawnSize.getChance() < fireworksIfChanceUnder;

                        if (fireworks) {
                            FireworkLauncher fireworkLauncher = new FireworkLauncher(main);
                            fireworkLauncher.launchFirework(player);
                        }

                        soManager.enhancePlayerBasedOnSizeObject(player, drawnSize);
                    }
                }.runTaskLater(main, PAUSE_DURATION);
                stopAnimation(player);
                return;
            }

            int nextDelay = Math.min(MAX_DELAY, delay);
            if (frame >= 12) {
                nextDelay = Math.min(MAX_DELAY, delay + 2);
            }
            new AnimationTask(player, inv, nextDelay, frame + 1).runTaskLater(main, delay);

            cancel();
        }
    }

    private void animateFrame(Inventory inv) {
        ItemStack[] items = inv.getContents();
        ItemStack[] itemsCopy = items;

        for (int i = 11; i < 15; i++) {
            items[i] = itemsCopy[i + 1];
        }

        items[15] = soManager.returnRandomItem(soManager.generateItemPool());

        inv.setContents(items);
    }
}

