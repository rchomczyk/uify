package pl.auroramc.ui.scene.renderer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import pl.auroramc.ui.canvas.paginated.PaginatedCanvas;
import pl.auroramc.ui.canvas.paginated.PaginatedCanvasRenderer;
import pl.auroramc.ui.scene.Scene;
import pl.auroramc.ui.scene.SceneImpl;
import pl.auroramc.ui.canvas.Canvas;
import pl.auroramc.ui.canvas.element.CanvasElement;
import pl.auroramc.ui.canvas.layout.LayoutCanvas;
import pl.auroramc.ui.canvas.layout.LayoutCanvasRenderer;
import pl.auroramc.ui.scene.inventory.SceneInventoryHolder;

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

        final Map<Integer, CanvasElement> renderedElements = new HashMap<>();
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

        Map<Integer, CanvasElement> renderCanvas(
            final SceneInventoryHolder sceneInventoryHolder,
            final Scene parentScene,
            final Canvas parentCanvas);
    }
}
