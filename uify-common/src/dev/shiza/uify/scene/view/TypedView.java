package dev.shiza.uify.scene.view;

import org.bukkit.event.inventory.InventoryType;

public record TypedView(InventoryType inventoryType, int rows, int columnsPerRow) implements SceneView {

    public static TypedView of(final InventoryType type, final int rows, final int columnsPerRow) {
        return new TypedView(type, rows, columnsPerRow);
    }

    public static TypedView ofHopper() {
        return new TypedView(InventoryType.HOPPER, 1, 5);
    }

    public static TypedView ofDropper() {
        return new TypedView(InventoryType.DROPPER, 3, 3);
    }

    @Override
    public int estimatedSize() {
        return rows * columnsPerRow;
    }
}
