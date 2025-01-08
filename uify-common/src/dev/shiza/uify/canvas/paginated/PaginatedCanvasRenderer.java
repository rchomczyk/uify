package dev.shiza.uify.canvas.paginated;

import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.paginated.PaginatedCanvasImpl.PrecalculatedSlotIndex;
import dev.shiza.uify.canvas.renderer.CanvasRenderer;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public final class PaginatedCanvasRenderer implements CanvasRenderer<PaginatedCanvas> {

    @Override
    public Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final PaginatedCanvas parentCanvas) {
        return renderCanvas(inventory, sceneInventoryHolder, parentScene, (PaginatedCanvasImpl) parentCanvas);
    }

    private Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final PaginatedCanvasImpl parentCanvas) {
        final Map<Position, CanvasElement> mutableBindingsByPosition = new HashMap<>();

        final List<List<CanvasElement>> partitionedElements = parentCanvas.partitionedElements();
        final List<CanvasElement> currentPartition = partitionedElements.isEmpty()
            ? Collections.emptyList()
            : partitionedElements.get(parentCanvas.currentPage());

        final PrecalculatedSlotIndex[] precalculatedSlotIndexes = parentCanvas.precalculatedSlotIndexes();
        final int elementsCount = currentPartition.size();
        for (int index = 0; index < elementsCount; index++) {
            final PrecalculatedSlotIndex precalculatedSlotIndex = precalculatedSlotIndexes[index];
            mutableBindingsByPosition.put(
                new Position(precalculatedSlotIndex.row(), precalculatedSlotIndex.column()),
                currentPartition.get(index));
        }

        return renderCanvasElements(
            inventory, sceneInventoryHolder, parentScene, parentCanvas, mutableBindingsByPosition);
    }
}
