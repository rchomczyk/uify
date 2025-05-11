package dev.shiza.uify.canvas.element.cooldown;

import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.canvas.element.CanvasElement;
import dev.shiza.uify.canvas.element.cooldown.expiration.ExpiringMap;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.bukkit.entity.HumanEntity;

final class CooldownService implements CooldownFacade {

    private final ExpiringMap<CooldownCompositeKey, Instant> cooldowns;

    CooldownService(final ExpiringMap<CooldownCompositeKey, Instant> cooldowns) {
        this.cooldowns = cooldowns;
    }

    @Override
    public void applyCooldown(final CanvasElement element, final HumanEntity viewer) {
        applyCooldown(element, viewer, ((CanvasBaseElement) element).cooldown());
    }

    @Override
    public boolean isOnCooldown(final CanvasElement element, final HumanEntity viewer) {
        final Instant now = Instant.now();
        return cooldowns.containsKey(new CooldownCompositeKey(element, viewer.getUniqueId())) &&
            cooldowns.get(new CooldownCompositeKey(element, viewer.getUniqueId())).isAfter(now);
    }

    @Override
    public Duration getRemainingCooldown(final CanvasElement element, final HumanEntity viewer) {
        final Instant now = Instant.now();
        return Optional.ofNullable(cooldowns.get(new CooldownCompositeKey(element, viewer.getUniqueId())))
            .map(expirationTime -> Duration.between(now, expirationTime))
            .orElse(Duration.ZERO);
    }

    private void applyCooldown(final CanvasElement element, final HumanEntity viewer, final Duration period) {
        final Instant now = Instant.now();
        cooldowns.put(new CooldownCompositeKey(element, viewer.getUniqueId()), now.plus(period), period);
    }
}
