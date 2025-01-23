package dev.shiza.uify.canvas.element.behaviour.cooldown;

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
