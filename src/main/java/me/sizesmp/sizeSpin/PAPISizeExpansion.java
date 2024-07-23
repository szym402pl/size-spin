package me.sizesmp.sizeSpin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPISizeExpansion extends PlaceholderExpansion {

    private final Main main; //


    public PAPISizeExpansion(Main main) {
        this.main = main;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return main.getDescription().getAuthors().getFirst();
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "sizesmp";
    }

    @Override
    @NotNull
    public String getVersion() {
        return main.getDescription().getVersion(); //


    }

    @Override
    public boolean persist() {
        return true; //


    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equalsIgnoreCase("size")) {
            return String.valueOf((player.getAttribute(Attribute.GENERIC_SCALE).getBaseValue() * 2));
        }

        return null;
    }
}
