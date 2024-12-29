package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.element.behaviour.CanvasElementBehaviourState;
import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public final class CanvasElementInteractionListener implements Listener {

    @EventHandler
    public void delegateDragEvent(final InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder) {
            final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

            final int sceneSize = scene.view().estimatedSize() - 1;
            event.getRawSlots().forEach(rawSlot -> {
                if (rawSlot < sceneSize) {
                    event.setCancelled(true);
                }

                final IdentifiedCanvasElement identifiedElement = sceneInventoryHolder.renderedElements().get(rawSlot);
                if (identifiedElement != null && identifiedElement.element() instanceof CanvasBaseElement canvasBaseElement) {
                    canvasBaseElement.elementDragConsumer().accept(
                        new CanvasElementBehaviourState<>(
                            sceneInventoryHolder,
                            identifiedElement.canvas(),
                            canvasBaseElement),
                        event);
                }
            });
        }
    }

    @EventHandler
    public void delegateClickEvent(final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder) {
            final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

            final int sceneSize = scene.view().estimatedSize() - 1;
            final int rawSlot = event.getRawSlot();
            if (rawSlot < sceneSize) {
                event.setCancelled(true);
            }

            final IdentifiedCanvasElement identifiedElement = sceneInventoryHolder.renderedElements().get(rawSlot);
            if (identifiedElement != null && identifiedElement.element() instanceof CanvasBaseElement canvasBaseElement) {
                canvasBaseElement.elementClickConsumer().accept(
                    new CanvasElementBehaviourState<>(
                        sceneInventoryHolder,
                        identifiedElement.canvas(),
                        canvasBaseElement),
                    event);
            }
        }
    }
}
