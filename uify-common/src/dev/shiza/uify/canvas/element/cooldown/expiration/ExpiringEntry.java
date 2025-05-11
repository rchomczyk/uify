package dev.shiza.uify.canvas.element.cooldown.expiration;

import java.time.Instant;

record ExpiringEntry<V>(V value, Instant expirationTime) {

    boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }
}
