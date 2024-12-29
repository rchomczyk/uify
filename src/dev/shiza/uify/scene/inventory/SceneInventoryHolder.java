package dev.shiza.uify.scene.inventory;

import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;

public final class SceneInventoryHolder implements InventoryHolder {

    private final Scene sceneMorph;
    private final Inventory inventory;
    private Map<Integer, IdentifiedCanvasElement> renderedElements;

    public SceneInventoryHolder(final Scene sceneMorph) {
        this.sceneMorph = sceneMorph;
        this.inventory = createInventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public Scene sceneMorph() {
        return sceneMorph;
    }

    public Map<Integer, IdentifiedCanvasElement> renderedElements() {
        return renderedElements;
    }

    public void renderedElements(final Map<Integer, IdentifiedCanvasElement> renderedElements) {
        this.renderedElements = renderedElements;
    }

    private Inventory createInventory() {
        final SceneImpl scene = (SceneImpl) sceneMorph;
        return Bukkit.createInventory(this, scene.view().estimatedSize(), scene.title());
    }
}
