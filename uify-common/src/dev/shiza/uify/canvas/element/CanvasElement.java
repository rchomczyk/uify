package dev.shiza.uify.canvas.element;

import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public interface CanvasElement extends CanvasElementBehaviours {

    ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas);

    void transform(final ItemStack itemStack);

    void transform(final Supplier<ItemStack> itemStack);

    void updateScene();

    @ApiStatus.Internal
    void assignSceneHolder(final SceneInventoryHolder sceneInventoryHolder);
}
