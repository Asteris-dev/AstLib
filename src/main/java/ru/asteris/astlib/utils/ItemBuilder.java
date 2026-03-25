package ru.asteris.astlib.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        if (meta != null && name != null) {
            meta.displayName(ColorUtils.parse(name).decoration(TextDecoration.ITALIC, false));
        }
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        if (meta != null && lore != null) {
            List<Component> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ColorUtils.parse(line).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(coloredLore);
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (meta != null && lore != null) {
            List<Component> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ColorUtils.parse(line).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(coloredLore);
        }
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        if (meta != null) {
            if (glowing) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.removeEnchant(Enchantment.DURABILITY);
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        if (meta != null) {
            meta.setCustomModelData(data);
        }
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (meta != null) {
            meta.setUnbreakable(unbreakable);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setSkullOwner(String owner) {
        if (meta instanceof SkullMeta && owner != null) {
            ((SkullMeta) meta).setOwner(owner);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer player) {
        if (meta instanceof SkullMeta && player != null) {
            ((SkullMeta) meta).setOwningPlayer(player);
        }
        return this;
    }

    public ItemStack build() {
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }
}