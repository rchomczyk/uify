package dev.shiza.uify.canvas.element.behaviour.cooldown;

import dev.shiza.uify.canvas.element.CanvasElement;
import java.time.Duration;
import org.bukkit.entity.HumanEntity;

public interface CooldownFacade {
    
    void applyCooldown(final CanvasElement element, final HumanEntity viewer);

    boolean isOnCooldown(final CanvasElement element, final HumanEntity viewer);

    Duration getRemainingCooldown(final CanvasElement element, final HumanEntity viewer);
}
