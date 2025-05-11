package dev.shiza.uify.event;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public final class InventoryEvents {

    private InventoryEvents() {}

    public static InventoryClickEvent dummyClickEvent(final HumanEntity viewer) {
        return new InventoryClickEvent(
            viewer.getOpenInventory(), InventoryType.SlotType.OUTSIDE, 999, ClickType.LEFT, InventoryAction.NOTHING);
    }
}
