package dev.shiza.uify.canvas.element;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.behaviour.CanvasElementGenericBehaviour;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface CanvasElementBehaviours {

    CanvasElement onElementDrag(
        final CanvasElementGenericBehaviour<Canvas, InventoryDragEvent> elementDragBehaviour);

    CanvasElement onElementClick(
        final CanvasElementGenericBehaviour<Canvas, InventoryClickEvent> elementClickBehaviour);
}
