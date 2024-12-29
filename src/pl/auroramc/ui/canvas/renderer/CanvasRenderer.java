package pl.auroramc.ui.canvas.renderer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.auroramc.ui.canvas.CanvasWithPosition.CanvasPosition;
import pl.auroramc.ui.position.PositionUtils;
import pl.auroramc.ui.scene.Scene;
import pl.auroramc.ui.scene.SceneImpl;
import pl.auroramc.ui.canvas.CanvasWithPosition;
import pl.auroramc.ui.canvas.element.CanvasElement;
import pl.auroramc.ui.position.Position;
import pl.auroramc.ui.scene.inventory.SceneInventoryHolder;

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
