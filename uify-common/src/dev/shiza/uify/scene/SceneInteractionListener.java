package dev.shiza.uify.scene;

import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviourState;
import dev.shiza.uify.scene.behaviour.SceneGenericBehaviourState;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class SceneInteractionListener implements Listener {

    @EventHandler
    public void delegateInventoryOpen(final InventoryOpenEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder &&
            sceneInventoryHolder.sceneMorph() instanceof SceneImpl scene) {
            scene.sceneDispatchBehaviour().accept(new SceneGenericBehaviourState(sceneInventoryHolder), event);
        }
    }

    @EventHandler
    public void delegateInventoryClose(final InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder &&
            sceneInventoryHolder.sceneMorph() instanceof SceneImpl scene) {
            scene.sceneCloseBehaviour().accept(new SceneGenericBehaviourState(sceneInventoryHolder), event);
            for (final Canvas canvas : scene.canvases()) {
                ((BaseCanvas) canvas).canvasCloseBehaviour()
                    .accept(new CanvasGenericBehaviourState<>(sceneInventoryHolder, scene, canvas), event);
            }
        }
    }
}
