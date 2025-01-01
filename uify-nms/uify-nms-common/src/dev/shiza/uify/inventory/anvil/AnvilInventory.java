package dev.shiza.uify.inventory.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface AnvilInventory {

    void open();

    void holder(final InventoryHolder holder);
    
    void renameText(final String renameText);

    String renameText();

    @NotNull
    Inventory getBukkitInventory();
}
