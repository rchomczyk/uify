package dev.shiza.uify.canvas.layout;

import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.renderer.CanvasRenderer;
import dev.shiza.uify.canvas.renderer.CanvasRenderingException;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

final class LayoutCanvasRenderer implements CanvasRenderer<LayoutCanvas> {

    private static final String ROW_DELIMITER = "\n";
    private static final String COLUMN_DELIMITER = "";
    private static final char COLUMN_UNDEFINED = ' ';

    LayoutCanvasRenderer() {}

    @Override
    public Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final LayoutCanvas parentCanvas) {
        return renderCanvas(inventory, parentScene, sceneInventoryHolder, (LayoutCanvasImpl) parentCanvas);
    }

    private Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final Scene parentScene,
        final SceneInventoryHolder sceneInventoryHolder,
        final LayoutCanvasImpl parentCanvas) {
        final Map<Position, CanvasElement> mutableBindingsByPosition = new HashMap<>(parentCanvas.bindingsByPosition());

        final String pattern = parentCanvas.pattern();
        final String[] rowPatterns = pattern.split(ROW_DELIMITER);

        final int rows = rowPatterns.length;
        for (int row = 0; row < rows; row++) {
            final String rowPattern = rowPatterns[row];

            final int columns = rowPattern.split(COLUMN_DELIMITER).length;
            for (int column = 0; column < columns; column++) {
                final char source = rowPattern.charAt(column);
                if (source == COLUMN_UNDEFINED) {
                    continue;
                }

                final CanvasElement element = parentCanvas.bindingsBySymbol().get(source);
                if (element == null) {
                    throw new CanvasRenderingException(
                        "Could not render layout canvas, because of missing binding for '%s' symbol in pattern:%n%s".formatted(
                            source,
                            pattern));
                }

                mutableBindingsByPosition.put(new Position(row, column), element);
            }
        }

        return renderCanvasElements(
            inventory, sceneInventoryHolder, parentScene, parentCanvas, mutableBindingsByPosition);
    }

    static final class InstanceHolder {

        static final CanvasRenderer<LayoutCanvas> RENDERER = new LayoutCanvasRenderer();
        static final CanvasMapperRenderer MAPPER = (holder, scene, canvas) ->
            RENDERER.renderCanvas(holder.getInventory(), holder, scene, (LayoutCanvas) canvas);

        private InstanceHolder() {}
    }
}
