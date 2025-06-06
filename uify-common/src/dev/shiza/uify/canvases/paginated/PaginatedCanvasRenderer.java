package dev.shiza.uify.canvases.paginated;

import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.element.IdentifiedCanvasElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.renderer.CanvasRenderer;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.Scene;

final class PaginatedCanvasRenderer implements CanvasRenderer<PaginatedCanvas> {

    PaginatedCanvasRenderer() {}

    @Override
    public Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory, final Scene parentScene, final PaginatedCanvas parentCanvas) {
        return renderCanvas(inventory, parentScene, (PaginatedCanvasImpl) parentCanvas);
    }

    private Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory, final Scene parentScene, final PaginatedCanvasImpl parentCanvas) {
        final int lastPartition = parentCanvas.partitionedElements().size() - 1;
        if (parentCanvas.currentPage() > lastPartition) {
            parentCanvas.page(lastPartition);
        }

        final Map<Position, CanvasElement> mutableBindingsByPosition = new HashMap<>();

        final List<List<CanvasElement>> partitionedElements = parentCanvas.partitionedElements();
        final List<CanvasElement> currentPartition = partitionedElements.isEmpty()
            ? Collections.emptyList()
            : partitionedElements.get(parentCanvas.currentPage());

        final Position[] precalculatedSlotIndexes = parentCanvas.precalculatedSlotIndexes();
        final int elementsCount = currentPartition.size();
        for (int index = 0; index < elementsCount; index++) {
            final Position precalculatedSlotIndex = precalculatedSlotIndexes[index];
            mutableBindingsByPosition.put(
                new Position(precalculatedSlotIndex.row(), precalculatedSlotIndex.column()),
                currentPartition.get(index));
        }

        final PaginatedCanvasImpl.NavigationalItemBinding forwardItemBinding = parentCanvas.forwardItemBinding();
        if (forwardItemBinding != null) {
            mutableBindingsByPosition.put(
                new Position(forwardItemBinding.row(), forwardItemBinding.column()),
                forwardItemBinding.element());
        }

        final PaginatedCanvasImpl.NavigationalItemBinding backwardItemBinding = parentCanvas.backwardItemBinding();
        if (backwardItemBinding != null) {
            mutableBindingsByPosition.put(
                new Position(backwardItemBinding.row(), backwardItemBinding.column()),
                backwardItemBinding.element());
        }

        return renderCanvasElements(
            inventory, parentScene, parentCanvas, mutableBindingsByPosition);
    }

    static final class InstanceHolder {

        static final CanvasRenderer<PaginatedCanvas> RENDERER = new PaginatedCanvasRenderer();
        static final CanvasMapperRenderer MAPPER = (holder, scene, canvas) ->
            RENDERER.renderCanvas(holder.getInventory(), scene, (PaginatedCanvas) canvas);

        private InstanceHolder() {}
    }
}
