package pl.auroramc.ui.canvas.element;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AsyncCanvasElement extends ImmutableCanvasElement {

    private static final ItemStack EMPTY_ITEM_STACK = new ItemStack(Material.AIR);

    public AsyncCanvasElement(
        final Supplier<ItemStack> loaderItemStack, final CompletableFuture<ItemStack> promisedItemStack) {
        super(loaderItemStack);
        this.handleAsyncCompletion(promisedItemStack);
    }

    public AsyncCanvasElement(final CompletableFuture<ItemStack> promisedItemStack) {
        this(() -> EMPTY_ITEM_STACK, promisedItemStack);
    }

    private void handleAsyncCompletion(final CompletableFuture<ItemStack> promisedItemStack) {
        promisedItemStack
            .thenAccept(itemStack -> itemStack(() -> itemStack))
            .thenAccept(__ -> updateScene());
    }
}
