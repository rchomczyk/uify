package dev.shiza.uify.scene.view;

import org.bukkit.event.inventory.InventoryType;

public interface SceneView {

    InventoryType inventoryType();

    int columnsPerRow();

    int estimatedSize();
}
