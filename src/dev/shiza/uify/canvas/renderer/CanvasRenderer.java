package dev.shiza.uify.canvas.renderer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.canvas.CanvasWithPosition.CanvasPosition;
import dev.shiza.uify.position.PositionUtils;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.canvas.CanvasWithPosition;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public interface CanvasRenderer<T> {

    Map<Integer, CanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final T parentCanvas);

    default Map<Integer, CanvasElement> renderCanvasElements(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final CanvasWithPosition parentCanvas,
        final Map<Position, CanvasElement> bindingsByPosition) {
        final Map<Integer, CanvasElement> renderedElements = new HashMap<>();

        final int columnsPerRow = ((SceneImpl) parentScene).view().columnsPerRow();
        bindingsByPosition.forEach((localPosition, element) -> {
            final CanvasPosition canvasPosition = parentCanvas.position();

            final int slotIndex = getSlotIndex(canvasPosition, localPosition, columnsPerRow);
            renderedElements.put(slotIndex, element);

            final ItemStack renderedItemStack = element.renderElement(parentScene, parentCanvas);
            inventory.setItem(slotIndex, renderedItemStack);

            element.assignSceneHolder(sceneInventoryHolder);
        });

        return renderedElements;
    }

    private int getSlotIndex(
        final CanvasPosition canvasPosition,
        final Position localPosition,
        final int columnsPerRow) {
        return hasPredefinedBounds(canvasPosition)
            ? PositionUtils.calculateSlot(canvasPosition, localPosition, columnsPerRow)
            : PositionUtils.calculateSlot(localPosition.row(), localPosition.column(), columnsPerRow);
    }

    private boolean hasPredefinedBounds(final CanvasPosition canvasPosition) {
        return canvasPosition.minimum() != null && canvasPosition.maximum() != null;
    }
}
