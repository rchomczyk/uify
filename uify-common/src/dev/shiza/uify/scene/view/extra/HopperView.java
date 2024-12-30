package dev.shiza.uify.scene.view.extra;

import dev.shiza.uify.scene.view.SceneView;
import org.bukkit.event.inventory.InventoryType;

public record HopperView() implements SceneView {

    public static HopperView of() {
        return new HopperView();
    }

    @Override
    public InventoryType inventoryType() {
        return InventoryType.HOPPER;
    }

    @Override
    public int columnsPerRow() {
        return 5;
    }

    @Override
    public int estimatedSize() {
        return columnsPerRow();
    }
}
