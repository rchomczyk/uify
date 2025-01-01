package dev.shiza.uify.inventory.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface AnvilInventory {

    String renameText();

    void renameText(final String renameText);

    void open();

    void close();

    void holder(final InventoryHolder holder);

    @NotNull
    Inventory getBukkitInventory();
}
