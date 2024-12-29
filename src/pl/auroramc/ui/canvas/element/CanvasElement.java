package pl.auroramc.ui.canvas.element;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import pl.auroramc.ui.scene.Scene;
import pl.auroramc.ui.canvas.Canvas;
import pl.auroramc.ui.scene.inventory.SceneInventoryHolder;
import pl.auroramc.ui.sdk.TriConsumer;

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
