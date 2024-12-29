package dev.shiza.uify.scene.renderer;

import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.canvas.paginated.PaginatedCanvas;
import dev.shiza.uify.canvas.paginated.PaginatedCanvasRenderer;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.layout.LayoutCanvas;
import dev.shiza.uify.canvas.layout.LayoutCanvasRenderer;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

final class SceneRendererImpl implements SceneRenderer {

    private final Map<Class<? extends Canvas>, CanvasMapperRenderer> renderers;

    SceneRendererImpl() {
        final LayoutCanvasRenderer layoutCanvasRenderer = new LayoutCanvasRenderer();
        final PaginatedCanvasRenderer paginatedCanvasRenderer = new PaginatedCanvasRenderer();
        this.renderers = Map.of(
            LayoutCanvas.class, (holder, scene, canvas) ->
                layoutCanvasRenderer.renderCanvas(holder.getInventory(), holder, scene, (LayoutCanvas) canvas),
            PaginatedCanvas.class, (holder, scene, canvas) ->
                paginatedCanvasRenderer.renderCanvas(holder.getInventory(), holder, scene, (PaginatedCanvas) canvas));
    }

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
            final CanvasMapperRenderer canvasMapperRenderer = renderer(canvas.getClass());
            renderedElements.putAll(canvasMapperRenderer.renderCanvas(sceneInventoryHolder, scene, canvas));
        }

        sceneInventoryHolder.renderedElements(renderedElements);
        return sceneInventoryHolder;
    }

    private CanvasMapperRenderer renderer(final Class<? extends Canvas> canvasClass) {
        final CanvasMapperRenderer canvasMapperRenderer = renderers.get(canvasClass);
        if (canvasMapperRenderer != null) {
            return canvasMapperRenderer;
        }

        for (final Map.Entry<Class<? extends Canvas>, CanvasMapperRenderer> entry : renderers.entrySet()) {
            if (entry.getKey().isAssignableFrom(canvasClass) || canvasClass.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }

        throw new SceneRenderingException("Could not find renderer for canvas class '%s'".formatted(canvasClass));
    }

    @FunctionalInterface
    private interface CanvasMapperRenderer {

        Map<Integer, IdentifiedCanvasElement> renderCanvas(
            final SceneInventoryHolder sceneInventoryHolder, final Scene parentScene, final Canvas parentCanvas);
    }
}
