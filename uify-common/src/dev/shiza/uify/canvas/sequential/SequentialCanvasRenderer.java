package dev.shiza.uify.canvas.sequential;

import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.renderer.CanvasRenderer;
import dev.shiza.uify.canvas.renderer.CanvasRenderingException;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;

public final class SequentialCanvasRenderer implements CanvasRenderer<SequentialCanvas> {

    @Override
    public Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final SequentialCanvas parentCanvas) {
        return renderCanvas(inventory, sceneInventoryHolder, parentScene, (SequentialCanvasImpl) parentCanvas);
    }

    private Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final SequentialCanvasImpl parentCanvas) {
        final int requiredSize = parentCanvas.elements().size();
        final int estimatedSize = estimatedSize((SceneImpl) parentScene, parentCanvas);
        if (requiredSize > estimatedSize) {
            throw new CanvasRenderingException(
                "Could not render sequential canvas because of insufficient size, required: %d, estimated size: %d".formatted(
                    requiredSize, estimatedSize));
        }

        final Map<Position, CanvasElement> elementsByPosition = new HashMap<>();

        int currentRow = 0;
        int currentColumn = 0;
        for (final CanvasElement element : parentCanvas.elements()) {
            elementsByPosition.put(new Position(currentRow, currentColumn), element);

            currentColumn++;
            if (currentColumn >= parentCanvas.position().maximum().column()) {
                currentColumn = 0;
                currentRow++;
            }
        }

        return renderCanvasElements(inventory, sceneInventoryHolder, parentScene, parentCanvas, elementsByPosition);
    }

    private int estimatedSize(final SceneImpl scene, final SequentialCanvasImpl parentCanvas) {
        final CanvasPosition position = parentCanvas.position();
        if (hasPredefinedBounds(position)) {
            final Position minimum = parentCanvas.position().minimum();
            final Position maximum = parentCanvas.position().maximum();
            return (maximum.row() - minimum.row() + 1) * (maximum.column() - minimum.column() + 1);
        }

        return scene.view().estimatedSize();
    }

    private boolean hasPredefinedBounds(final CanvasPosition canvasPosition) {
        return canvasPosition.minimum() != null && canvasPosition.maximum() != null;
    }
}
