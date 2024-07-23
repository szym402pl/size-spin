package me.sizesmp.sizeSpin.Utils;

import me.sizesmp.sizeSpin.Main;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireworkLauncher {

    private Main main;

    public FireworkLauncher(Main main) {
        this.main = main;
    }

    public void launchFirework(Player player) {
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection();
        Location fireworkLocation = playerLocation.add(direction.multiply(2)).add(1, 2, 0);

        new BukkitRunnable() {
            @Override
            public void run() {
                Firework firework = (Firework) player.getWorld().spawnEntity(fireworkLocation, EntityType.FIREWORK_ROCKET);
                FireworkMeta meta = firework.getFireworkMeta();

                FireworkEffect effect = FireworkEffect.builder()
                        .withColor(Color.RED, Color.BLUE)
                        .withFade(Color.GREEN)
                        .with(FireworkEffect.Type.BALL)
                        .trail(true)
                        .flicker(true)
                        .build();
                meta.addEffect(effect);
                meta.setPower(1);
                firework.setFireworkMeta(meta);
            }
        }.runTaskLater(main, 4L);
    }
}