package dev.shiza.uify.canvas.behaviour;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.scene.Scene;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;

public record CanvasGenericBehaviourState<T extends Canvas>(SceneInventoryHolder holder, Scene scene, T canvas) {

}
