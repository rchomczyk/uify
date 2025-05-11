package dev.shiza.uify.inventory.item;

import com.destroystokyo.paper.MaterialSetTag;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class ItemStacks {

    public static final MaterialSetTag LEATHER_ARMOR = new MaterialSetTag(keyFor("leather_armor"))
        .add(Material.LEATHER_HELMET)
        .add(Material.LEATHER_CHESTPLATE)
        .add(Material.LEATHER_LEGGINGS)
        .add(Material.LEATHER_BOOTS)
        .lock();

    private ItemStacks() {}

    public static ItemStack namelessItem(final Material material) {
        return ItemStackBuilder.of(material)
            .displayName(Component.text(" "))
            .flag(ItemFlag.values())
            .build();
    }

    public static ItemStack leatherArmor(final Material material) {
        return leatherArmor(material, randomBukkitColor());
    }

    public static ItemStack leatherArmor(final Material material, final Color color) {
        if (!LEATHER_ARMOR.isTagged(material)) {
            throw new ItemStackBuildingException("Could not get colored leather armor, because of invalid material.");
        }

        return ItemStackBuilder.of(material)
            .manipulateItemMeta(itemMeta -> ((LeatherArmorMeta) itemMeta).setColor(color))
            .build();
    }

    public static Color randomBukkitColor() {
        final java.awt.Color randomHSBColor = randomHSBColor();
        return Color.fromRGB(randomHSBColor.getRed(), randomHSBColor.getGreen(), randomHSBColor.getBlue());
    }

    public static java.awt.Color randomHSBColor() {
        float hue = ThreadLocalRandom.current().nextFloat();
        float saturation = ThreadLocalRandom.current().nextFloat();
        float brightness = ThreadLocalRandom.current().nextFloat();
        return java.awt.Color.getHSBColor(hue, saturation, brightness);
    }

    private static NamespacedKey keyFor(final String key) {
        return new NamespacedKey("paper", key + "_settag");
    }
}
