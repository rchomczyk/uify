package dev.shiza.uify.item;

import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.element.ImmutableCanvasElement;
import java.util.List;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public final class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemStackBuilder(final ItemStack itemStack, final ItemMeta itemMeta) {
        this.itemStack = itemStack;
        this.itemMeta = itemMeta;
    }

    public static ItemStackBuilder of(final ItemStack itemStack) {
        return new ItemStackBuilder(itemStack, itemStack.getItemMeta());
    }

    public static ItemStackBuilder of(final Material material) {
        return of(new ItemStack(material));
    }

    public ItemStackBuilder displayName(final Component displayName) {
        itemMeta.displayName(displayName);
        return this;
    }

    public ItemStackBuilder lore(final Component... lore) {
        itemMeta.lore(List.of(lore));
        return this;
    }

    public ItemStackBuilder flag(final ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemStackBuilder amount(final int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder unbreakable(final boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemStackBuilder enchantment(final Enchantment enchantment, final int level, final boolean unsafe) {
        itemMeta.addEnchant(enchantment, level, unsafe);
        return this;
    }

    public ItemStackBuilder enchantment(final Enchantment enchantment, final int level) {
        return enchantment(enchantment, level, false);
    }

    public ItemStackBuilder customModelData(final int customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public ItemStackBuilder persistentDataContainer(final Consumer<PersistentDataContainer> mutator) {
        mutator.accept(itemMeta.getPersistentDataContainer());
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public CanvasElement buildElement() {
        return new ImmutableCanvasElement(this::build);
    }
}
