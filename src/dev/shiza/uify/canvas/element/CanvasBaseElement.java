package dev.shiza.uify.canvas.element;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import dev.shiza.uify.scene.renderer.SceneRenderer;
import dev.shiza.uify.sdk.TriConsumer;

public class CanvasBaseElement implements CanvasElement {

    private Supplier<ItemStack> itemStack;
    private TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryDragEvent> elementDragConsumer =
        (holder, element, event) -> {};
    private TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryClickEvent> elementClickConsumer =
        (holder, element, event) -> {};
    private Set<SceneInventoryHolder> owners = Collections.emptySet();

    public CanvasBaseElement(final Supplier<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public CanvasBaseElement(
        final Supplier<ItemStack> itemStack,
        final TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryDragEvent> elementDragConsumer,
        final TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryClickEvent> elementClickConsumer) {
        this.itemStack = itemStack;
        this.elementDragConsumer = elementDragConsumer;
        this.elementClickConsumer = elementClickConsumer;
    }

    @Override
    public ItemStack renderElement(final Scene parentScene, final Canvas parentCanvas) {
        return itemStack.get();
    }

    @Override
    public CanvasElement onElementDrag(final TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryDragEvent> elementDragConsumer) {
        this.elementDragConsumer = this.elementDragConsumer.andThen(elementDragConsumer);
        return this;
    }

    @Override
    public CanvasElement onElementClick(final TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryClickEvent> elementClickConsumer) {
        this.elementClickConsumer = this.elementClickConsumer.andThen(elementClickConsumer);
        return this;
    }

    @Override
    public void assignSceneHolder(final SceneInventoryHolder sceneInventoryHolder) {
        final Set<SceneInventoryHolder> mutableOwners = new HashSet<>(owners);
        mutableOwners.add(sceneInventoryHolder);
        owners = Collections.unmodifiableSet(mutableOwners);
    }

    @Override
    public void updateScene() {
        owners.forEach(owner -> SceneRenderer.sceneRenderer().renderScene(owner.sceneMorph(), owner));
    }

    public void itemStack(final Supplier<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryDragEvent> elementDragConsumer() {
        return elementDragConsumer;
    }

    public TriConsumer<SceneInventoryHolder, CanvasBaseElement, InventoryClickEvent> elementClickConsumer() {
        return elementClickConsumer;
    }
}
