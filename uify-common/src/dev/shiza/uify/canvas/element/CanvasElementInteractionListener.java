package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.element.behaviour.CanvasElementGenericBehaviourState;
import dev.shiza.uify.canvas.element.behaviour.cooldown.CooldownFacade;
import dev.shiza.uify.canvas.element.behaviour.cooldown.CooldownFacadeFactory;
import dev.shiza.uify.canvas.element.behaviour.cooldown.CooldownGenericBehaviourState;
import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class CanvasElementInteractionListener implements Listener {

    private final CooldownFacade cooldownFacade;

    public CanvasElementInteractionListener() {
        this.cooldownFacade = CooldownFacadeFactory.cooldownFacade();
    }

    @EventHandler
    public void delegateDragEvent(final InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder) {
            final SceneImpl scene = (SceneImpl) sceneInventoryHolder.sceneMorph();

            final int sceneSize = scene.view().estimatedSize() - 1;
            event.getRawSlots().forEach(rawSlot -> {
                final IdentifiedCanvasElement identifiedElement = sceneInventoryHolder.renderedElements().get(rawSlot);
                if (identifiedElement != null &&
                    identifiedElement.element() instanceof CanvasBaseElement canvasBaseElement) {
                    canvasBaseElement.elementDragConsumer().accept(
                        new CanvasElementGenericBehaviourState<>(
                            sceneInventoryHolder,
                            identifiedElement.canvas(),
                            canvasBaseElement),
                        event);
                } else if (rawSlot < sceneSize) {
                    event.setCancelled(true);
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

            final IdentifiedCanvasElement identifiedElement = sceneInventoryHolder.renderedElements().get(rawSlot);
            if (identifiedElement != null &&
                identifiedElement.element() instanceof CanvasBaseElement canvasBaseElement) {
                final HumanEntity viewer = event.getWhoClicked();
                if (canvasBaseElement.cooldown().isPositive()) {
                    if (cooldownFacade.isOnCooldown(canvasBaseElement, viewer)) {
                        canvasBaseElement.elementCooldownBehaviour().accept(
                            new CooldownGenericBehaviourState<>(
                                sceneInventoryHolder,
                                identifiedElement.canvas(),
                                canvasBaseElement,
                                cooldownFacade.getRemainingCooldown(canvasBaseElement, viewer)),
                            event);
                        return;
                    }

                    cooldownFacade.applyCooldown(canvasBaseElement, viewer);
                }

                canvasBaseElement.elementClickConsumer().accept(
                    new CanvasElementGenericBehaviourState<>(
                        sceneInventoryHolder,
                        identifiedElement.canvas(),
                        canvasBaseElement),
                    event);
            } else if (rawSlot < sceneSize) {
                event.setCancelled(true);
            }
        }
    }
}
