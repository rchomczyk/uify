package dev.shiza.uify.canvas.element.behaviour.cooldown;

import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.time.Duration;

public record CooldownGenericBehaviourState<T extends Canvas>(
    SceneInventoryHolder holder, T canvas, CanvasBaseElement element, Duration remainingPeriod) {

}
