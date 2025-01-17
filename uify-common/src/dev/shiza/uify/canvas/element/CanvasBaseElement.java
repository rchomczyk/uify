package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.element.behaviour.CanvasElementGenericBehaviour;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;

public class CanvasBaseElement implements CanvasElement {

    private Supplier<ItemStack> itemStack;
    private CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragConsumer =
        (state, event) -> {};
    private CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickConsumer =
        (state, event) -> {};
    private final Set<Canvas> owners = new HashSet<>();

    public CanvasBaseElement(final Supplier<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public CanvasBaseElement(
        final Supplier<ItemStack> itemStack,
        final CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragConsumer,
        final CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickConsumer) {
        this.itemStack = itemStack;
        this.elementDragConsumer = elementDragConsumer;
        this.elementClickConsumer = elementClickConsumer;
    }

    public CanvasBaseElement(final CanvasElement element) {
        this(
            ((CanvasBaseElement) element).itemStack,
            ((CanvasBaseElement) element).elementDragConsumer,
            ((CanvasBaseElement) element).elementClickConsumer);
    }

    @Override
    public ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas) {
        return itemStack.get();
    }

    @Override
    public CanvasElement onElementDrag(final CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour) {
        this.elementDragConsumer = this.elementDragConsumer.andThen(elementDragBehaviour);
        return this;
    }

    @Override
    public CanvasElement onElementClick(final CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour) {
        this.elementClickConsumer = this.elementClickConsumer.andThen(elementClickBehaviour);
        return this;
    }

    @Override
    public void assign(final Canvas canvas) {
        owners.add(canvas);
    }

    @Override
    public void update() {
        owners.forEach(Canvas::update);
    }

    @Override
    public void transform(final Supplier<ItemStack> itemStack) {
        this.itemStack = itemStack;
        this.update();
    }

    public CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragConsumer() {
        return elementDragConsumer;
    }

    public CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickConsumer() {
        return elementClickConsumer;
    }
}
