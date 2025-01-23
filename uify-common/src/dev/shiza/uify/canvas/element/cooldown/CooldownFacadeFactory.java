package dev.shiza.uify.canvas.element.cooldown;

import net.jodah.expiringmap.ExpiringMap;

public final class CooldownFacadeFactory {

    private CooldownFacadeFactory() {}

    public static CooldownFacade cooldownFacade() {
        return new CooldownService(
            ExpiringMap.builder()
                .variableExpiration()
                .build());
    }
}
