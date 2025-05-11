package dev.shiza.uify.canvas;

import dev.shiza.uify.canvas.behaviour.CanvasGenericBehaviour;
import dev.shiza.uify.canvas.behaviour.tick.CanvasTickBehaviour;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CanvasBehaviours {

    Canvas onCanvasClose(final CanvasGenericBehaviour<Canvas, InventoryCloseEvent> canvasCloseBehaviour);

    <T extends Canvas> Canvas onCanvasTick(final Class<T> canvasType, final CanvasTickBehaviour<T> canvasTickBehaviour);
}
