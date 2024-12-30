package dev.shiza.uify.scene.view.extra;

import dev.shiza.uify.scene.view.SceneView;
import org.bukkit.event.inventory.InventoryType;

public record HopperView() implements SceneView {

    private static final int COLUMNS_PER_ROW = 5;

    public static HopperView of() {
        return new HopperView();
    }

    @Override
    public InventoryType inventoryType() {
        return InventoryType.HOPPER;
    }

    @Override
    public int columnsPerRow() {
        return COLUMNS_PER_ROW;
    }

    @Override
    public int estimatedSize() {
        return columnsPerRow();
    }
}
