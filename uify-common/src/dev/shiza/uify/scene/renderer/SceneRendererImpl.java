package dev.shiza.uify.scene.renderer;

import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

final class SceneRendererImpl implements SceneRenderer {

    SceneRendererImpl() {}

    @Override
    public SceneInventoryHolder renderScene(final Scene sceneMorph) {
        final SceneInventoryHolder sceneInventoryHolder = new SceneInventoryHolder(sceneMorph);
        return renderScene(sceneMorph, sceneInventoryHolder);
    }

    @Override
    public SceneInventoryHolder renderScene(final Scene sceneMorph, final SceneInventoryHolder sceneInventoryHolder) {
        final SceneImpl scene = (SceneImpl) sceneMorph;

        final Inventory sceneInventory = sceneInventoryHolder.getInventory();
        sceneInventory.clear();

        final Map<Integer, IdentifiedCanvasElement> renderedElements = new HashMap<>();
        for (final Canvas canvas : scene.canvases()) {
            renderedElements.putAll(canvas.mapper().renderCanvas(sceneInventoryHolder, scene, canvas));
        }

        sceneInventoryHolder.renderedElements(renderedElements);
        return sceneInventoryHolder;
    }
}
