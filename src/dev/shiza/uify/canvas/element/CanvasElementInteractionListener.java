package dev.shiza.uify.canvas.element;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public final class CanvasElementInteractionListener implements Listener {

    @EventHandler
    public void delegateClickEvent(final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder) {
            final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

            final int sceneSize = scene.view().estimatedSize() - 1;
            final int rawSlot = event.getRawSlot();

            final CanvasElement element = sceneInventoryHolder.renderedElements().get(rawSlot);
            if (element instanceof CanvasBaseElement canvasBaseElement) {
                canvasBaseElement.elementClickConsumer().accept(sceneInventoryHolder, canvasBaseElement, event);
                return;
            }

            if (rawSlot < sceneSize) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void delegateDragEvent(final InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder) {
            final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

            final int sceneSize = scene.view().estimatedSize() - 1;
            event.getRawSlots().forEach(rawSlot -> {
                final CanvasElement element = sceneInventoryHolder.renderedElements().get(rawSlot);
                if (element instanceof CanvasBaseElement canvasBaseElement) {
                    canvasBaseElement.elementDragConsumer().accept(sceneInventoryHolder, canvasBaseElement, event);
                    return;
                }

                if (rawSlot < sceneSize - 1) {
                    event.setCancelled(true);
                }
            });
        }
    }
}
