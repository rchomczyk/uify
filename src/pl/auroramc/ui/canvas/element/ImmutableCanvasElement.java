package pl.auroramc.ui.canvas.element;

import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ImmutableCanvasElement extends CanvasBaseElement {

    public ImmutableCanvasElement(final Supplier<ItemStack> itemStack) {
        super(
            itemStack,
            (holder, element, event) -> event.setCancelled(true),
            (holder, element, event) -> event.setCancelled(true));
    }

    public ImmutableCanvasElement(final Material material) {
        this(() -> new ItemStack(material));
    }
}
