package pl.auroramc.ui.canvas.layout;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import pl.auroramc.ui.scene.Scene;
import pl.auroramc.ui.canvas.element.CanvasElement;
import pl.auroramc.ui.canvas.renderer.CanvasRenderer;
import pl.auroramc.ui.canvas.renderer.CanvasRenderingException;
import pl.auroramc.ui.position.Position;
import pl.auroramc.ui.scene.inventory.SceneInventoryHolder;

public final class LayoutCanvasRenderer implements CanvasRenderer<LayoutCanvas> {

    private static final String ROW_DELIMITER = "\n";
    private static final String COLUMN_DELIMITER = "";
    private static final char COLUMN_UNDEFINED = ' ';

    @Override
    public Map<Integer, CanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final LayoutCanvas parentCanvas) {
        return renderCanvas(inventory, parentScene, sceneInventoryHolder, (LayoutCanvasImpl) parentCanvas);
    }

    private Map<Integer, CanvasElement> renderCanvas(
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
}
