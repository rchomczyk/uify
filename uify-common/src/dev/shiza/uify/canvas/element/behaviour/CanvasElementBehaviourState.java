package dev.shiza.uify.canvas.element.behaviour;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public record CanvasElementBehaviourState<T extends Canvas>(
    SceneInventoryHolder holder, T canvas, CanvasBaseElement element) {
}
