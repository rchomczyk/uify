package dev.shiza.uify.canvas.element;

import java.time.Duration;
import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public interface CanvasElement extends CanvasElementBehaviours {

    CanvasElement cooldown(final Duration period);

    ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas);

    @ApiStatus.Internal
    void assign(final Canvas canvas);

    void update();

    void transform(final Supplier<ItemStack> itemStack);

    void transform(final Plugin plugin, final Supplier<ItemStack> itemStack, final Duration period);

    void transform(
        final Plugin plugin, final Supplier<ItemStack> from, final Supplier<ItemStack> to, final Duration period);
}
