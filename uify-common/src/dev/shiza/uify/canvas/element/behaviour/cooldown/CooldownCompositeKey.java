package dev.shiza.uify.canvas.element.behaviour.cooldown;

import dev.shiza.uify.canvas.element.CanvasElement;
import java.util.UUID;

public record CooldownCompositeKey(CanvasElement element, UUID viewerUniqueId) {
}
