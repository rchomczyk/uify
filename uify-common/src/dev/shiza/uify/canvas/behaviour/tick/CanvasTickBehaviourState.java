package dev.shiza.uify.canvas.behaviour.tick;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public record CanvasTickBehaviourState<T extends Canvas>(SceneInventoryHolder holder, T canvas) {
}
