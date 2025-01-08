package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.element.identity.IdentifiedCanvasElement;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.util.Map;

@FunctionalInterface
public interface CanvasMapperRenderer {

    Map<Integer, IdentifiedCanvasElement> renderCanvas(
        final SceneInventoryHolder sceneInventoryHolder, final Scene parentScene, final Canvas parentCanvas);
}
