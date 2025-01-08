package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.element.behaviour.CanvasElementBehaviour;
import java.util.function.Supplier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public interface CanvasElement {

    ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas);

    CanvasElement onElementDrag(
        final CanvasElementBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour);

    CanvasElement onElementClick(
        final CanvasElementBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour);

    void transform(final ItemStack itemStack);

    void transform(final Supplier<ItemStack> itemStack);

    void updateScene();

    @ApiStatus.Internal
    void assignSceneHolder(final SceneInventoryHolder sceneInventoryHolder);
}
