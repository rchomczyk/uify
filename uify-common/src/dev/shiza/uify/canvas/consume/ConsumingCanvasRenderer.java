package dev.shiza.uify.canvas.consume;

import dev.shiza.uify.canvas.CanvasMapperRenderer;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import dev.shiza.uify.canvas.position.CanvasPosition;
import dev.shiza.uify.canvas.renderer.CanvasRenderer;
import dev.shiza.uify.canvas.renderer.CanvasRenderingException;
import dev.shiza.uify.position.Position;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.SceneImpl;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

final class ConsumingCanvasRenderer implements CanvasRenderer<ConsumingCanvas> {

    private static final CanvasBaseElement EMPTY_ELEMENT = new CanvasBaseElement(() -> new ItemStack(Material.AIR));

    ConsumingCanvasRenderer() {}

    @Override
    public Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final Scene parentScene,
        final ConsumingCanvas parentCanvas) {
        return renderCanvas(
            inventory,
            sceneInventoryHolder,
            (SceneImpl) parentScene,
            (ConsumingCanvasImpl) parentCanvas);
    }

    private Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final Inventory inventory,
        final SceneInventoryHolder sceneInventoryHolder,
        final SceneImpl parentScene,
        final ConsumingCanvasImpl parentCanvas) {
        final Map<Position, CanvasElement> mutableBindingsByPosition = new HashMap<>();

        final Collection<CanvasElement> elements = parentCanvas.elements();
        if (elements == null) {
            return Map.of();
        }

        final CanvasPosition canvasPosition = parentCanvas.position();
        if (canvasPosition == null) {
            throw new CanvasRenderingException("Could not render consuming canvas, because of missing position");
        }

        final Position minimum = canvasPosition.minimum();
        final Position maximum = canvasPosition.maximum();
        if (minimum == null || maximum == null) {
            throw new CanvasRenderingException("Could not render consuming canvas, because of missing position bounds");
        }

        final Iterator<CanvasElement> elementIterator = elements.iterator();

        final int rows = maximum.row() - minimum.row();
        final int columns = maximum.column() - minimum.column();
        for (int row = 0; row <= rows; row++) {
            for (int column = 0; column <= columns; column++) {
                final CanvasElement element = elementIterator.hasNext() ? elementIterator.next() : EMPTY_ELEMENT;
                mutableBindingsByPosition.put(new Position(row, column), element);
            }
        }

        return renderCanvasElements(
            inventory, sceneInventoryHolder, parentScene, parentCanvas, mutableBindingsByPosition);
    }

    static final class InstanceHolder {

        static final CanvasRenderer<ConsumingCanvas> RENDERER = new ConsumingCanvasRenderer();
        static final CanvasMapperRenderer MAPPER = (holder, scene, canvas) ->
            RENDERER.renderCanvas(holder.getInventory(), holder, scene, (ConsumingCanvas) canvas);

        private InstanceHolder() {}
    }
}
