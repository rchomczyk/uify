package pl.auroramc.ui.scene.inventory;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import pl.auroramc.ui.scene.Scene;
import pl.auroramc.ui.scene.SceneImpl;
import pl.auroramc.ui.canvas.element.CanvasElement;

public final class SceneInventoryHolder implements InventoryHolder {

    private final Scene sceneMorph;
    private final Inventory inventory;
    private Map<Integer, CanvasElement> renderedElements;

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

    public Map<Integer, CanvasElement> renderedElements() {
        return renderedElements;
    }

    public void renderedElements(final Map<Integer, CanvasElement> renderedElements) {
        this.renderedElements = renderedElements;
    }

    private Inventory createInventory() {
        final SceneImpl scene = (SceneImpl) sceneMorph;
        return Bukkit.createInventory(this, scene.view().estimatedSize(), scene.title());
    }
}
