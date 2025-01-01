package dev.shiza.uify.scene.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public record AnvilView(Player viewer) implements SceneView {

    private static final int COLUMNS_PER_ROW = 3;

    public static AnvilView ofViewer(final Player viewer) {
        return new AnvilView(viewer);
    }

    @Override
    public InventoryType inventoryType() {
        return InventoryType.ANVIL;
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
