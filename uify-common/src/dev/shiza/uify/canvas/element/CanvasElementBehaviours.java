package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.behaviour.CanvasElementBehaviour;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface CanvasElementBehaviours {

    CanvasElement onElementDrag(
        final CanvasElementBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour);

    CanvasElement onElementClick(
        final CanvasElementBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour);
}
