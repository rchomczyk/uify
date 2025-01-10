package dev.shiza.uify.canvas.renderer;

import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.position.PositionUtils;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.position.Position;

public interface CanvasRenderer<T> {

    Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory, final Scene parentScene, final T parentCanvas);

    default Map<Integer, IdentifiedCanvasElement> renderCanvasElements(
        final Inventory inventory,
        final Scene parentScene,
        final BaseCanvas parentCanvas,
        final Map<Position, CanvasElement> bindingsByPosition) {
        final Map<Integer, IdentifiedCanvasElement> previousElements = parentCanvas.renderedElements();
        final Map<Integer, IdentifiedCanvasElement> renderedElements = new HashMap<>();

        final int columnsPerRow = ((SceneImpl) parentScene).view().columnsPerRow();
        bindingsByPosition.forEach((localPosition, element) -> {
            final CanvasPosition canvasPosition = parentCanvas.position();

            final int slotIndex = getSlotIndex(canvasPosition, localPosition, columnsPerRow);
            renderedElements.put(slotIndex, new IdentifiedCanvasElement(parentCanvas, element));

            final ItemStack renderedItemStack = element.renderElement(parentScene, parentCanvas);
            inventory.setItem(slotIndex, renderedItemStack);

            element.assign(parentCanvas);
        });

        final Set<Integer> previousElementsSlotIndexes = new HashSet<>(previousElements.keySet());
        final Set<Integer> renderedElementsSlotIndexes = new HashSet<>(renderedElements.keySet());
        previousElementsSlotIndexes.removeAll(renderedElementsSlotIndexes);
        previousElementsSlotIndexes.forEach(slotIndex -> inventory.setItem(slotIndex, null));

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
