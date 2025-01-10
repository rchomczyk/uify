package dev.shiza.uify.canvas.element;

import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import org.jetbrains.annotations.ApiStatus;

public interface CanvasElement extends CanvasElementBehaviours {

    ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas);

    @ApiStatus.Internal
    void assign(final Canvas canvas);

    void update();

    void transform(final Supplier<ItemStack> itemStack);
}
