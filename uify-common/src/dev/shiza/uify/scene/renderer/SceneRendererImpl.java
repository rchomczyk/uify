package dev.shiza.uify.scene.renderer;

import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import java.util.HashMap;
import java.util.Map;
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

        final Map<Integer, IdentifiedCanvasElement> aggregatedElements = new HashMap<>();
        for (final Canvas canvas : scene.canvases()) {
            canvas.assign(sceneInventoryHolder);

            final Map<Integer, IdentifiedCanvasElement> renderedElements =
                canvas.mapper().renderCanvas(sceneInventoryHolder, scene, canvas);
            ((BaseCanvas) canvas).renderedElements(renderedElements);

            aggregatedElements.putAll(renderedElements);
        }

        sceneInventoryHolder.renderedElements(aggregatedElements);
        return sceneInventoryHolder;
    }
}
