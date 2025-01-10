package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CanvasBehaviours {

    Canvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);
}
