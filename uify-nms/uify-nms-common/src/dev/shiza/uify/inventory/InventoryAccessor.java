package dev.shiza.uify.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.InventoryHolder;

public interface InventoryAccessor {

    void title(final InventoryHolder holder, final Component title);
}
