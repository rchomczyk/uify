package dev.shiza.uify.canvas.element.cooldown;

import dev.shiza.uify.canvas.element.cooldown.expiration.ExpirationScheduler;
import dev.shiza.uify.canvas.element.cooldown.expiration.ExpiringMap;
import dev.shiza.uify.canvas.element.cooldown.expiration.PaperExpirationSchedulerAccessor;
import dev.shiza.uify.time.MinecraftTimeEquivalent;
import java.time.Duration;
import java.time.Instant;

public final class CooldownFacadeFactory {

    private CooldownFacadeFactory() {}

    public static CooldownFacade cooldownFacade() {
        final ExpirationScheduler expirationScheduler = PaperExpirationSchedulerAccessor.paperExpirationScheduler();
        final ExpiringMap<CooldownCompositeKey, Instant> expiringMap =
            new ExpiringMap<>(
                Duration.ZERO,
                Duration.ofMillis(MinecraftTimeEquivalent.MILLISECONDS_PER_TICK),
                expirationScheduler);
        expiringMap.expirationListener(new CooldownExpirationListener());
        return new CooldownService(expiringMap);
    }
}
