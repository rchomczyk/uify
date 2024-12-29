package dev.shiza.uify.canvas.element;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.sdk.TriConsumer;

public interface CanvasElement {

    ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas);

    CanvasElement onElementDrag(
        final TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryDragEvent> elementDragConsumer);

    CanvasElement onElementClick(
        final TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryClickEvent> elementClickConsumer);

    @ApiStatus.Internal
    void assignSceneHolder(final SceneInventoryHolder sceneInventoryHolder);

    void updateScene();
}
