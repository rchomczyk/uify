package dev.shiza.uify.scene.view;

import org.bukkit.event.inventory.InventoryType;

public record ChestView(int height, int width) implements SceneView {

    private static final int COLUMNS_PER_ROW = 9;

    public static ChestView ofRows(final int rows) {
        return new ChestView(rows, COLUMNS_PER_ROW);
    }

    @Override
    public InventoryType inventoryType() {
        return InventoryType.CHEST;
    }

    @Override
    public int columnsPerRow() {
        return COLUMNS_PER_ROW;
    }

    @Override
    public int estimatedSize() {
        return height * width;
    }
}
