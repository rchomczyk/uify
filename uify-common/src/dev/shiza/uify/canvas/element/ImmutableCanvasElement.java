package dev.shiza.uify.canvas.element;

import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ImmutableCanvasElement extends CanvasBaseElement {

    public ImmutableCanvasElement(final Supplier<ItemStack> itemStack) {
        super(
            itemStack,
            (state, event) -> event.setCancelled(true),
            (state, event) -> event.setCancelled(true),
            (state, event) -> event.setCancelled(true));
    }

    public ImmutableCanvasElement(final Material material) {
        this(() -> new ItemStack(material));
    }
}
