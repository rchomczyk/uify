package dev.shiza.uify.scene.inventory;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.IdentifiedCanvasElement;
import dev.shiza.uify.inventory.InventoryAccessorProvider;
import dev.shiza.uify.inventory.anvil.AnvilInventory;
import dev.shiza.uify.inventory.anvil.AnvilInventoryFactory;
import dev.shiza.uify.scene.view.AnvilView;
import dev.shiza.uify.scene.view.ChestView;
import dev.shiza.uify.scene.view.SceneView;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;

public final class SceneInventoryHolder implements InventoryHolder {

    private final Scene sceneMorph;
    private final Inventory inventory;
    private AnvilInventory anvilInventory;
    private Map<Integer, IdentifiedCanvasElement> renderedElements;

    public SceneInventoryHolder(final Scene sceneMorph) {
        this.sceneMorph = sceneMorph;
        this.inventory = createInventory();
    }

    public void title(final Component title) {
        InventoryAccessorProvider.inventoryAccessor().title(this, title);
    }

    public void update() {
        ((SceneImpl) sceneMorph).canvases().forEach(Canvas::update);
    }

    public Scene sceneMorph() {
        return sceneMorph;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public @NotNull AnvilInventory anvilInventory() {
        if (anvilInventory == null) {
            throw new SceneInventoryResolvingException(
                "Could not get anvil inventory from scene inventory holder, because it was null.");
        }

        return anvilInventory;
    }

    public Map<Integer, IdentifiedCanvasElement> renderedElements() {
        return renderedElements;
    }

    public void renderedElements(final Map<Integer, IdentifiedCanvasElement> renderedElements) {
        this.renderedElements = renderedElements;
    }

    private Inventory createInventory() {
        final SceneImpl scene = (SceneImpl) sceneMorph;
        final SceneView sceneView = scene.view();
        if (sceneView instanceof ChestView) {
            return Bukkit.createInventory(this, sceneView.estimatedSize(), scene.title());
        } else if (sceneView instanceof AnvilView(Player viewer)) {
            anvilInventory = AnvilInventoryFactory.anvilInventory(viewer, scene.title());
            anvilInventory.holder(this);
            return anvilInventory.getBukkitInventory();
        } else {
            return Bukkit.createInventory(this, sceneView.inventoryType(), scene.title());
        }
    }
}
