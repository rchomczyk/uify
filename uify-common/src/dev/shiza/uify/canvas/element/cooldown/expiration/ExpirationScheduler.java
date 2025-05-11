package dev.shiza.uify.canvas.element.cooldown.expiration;

import java.time.Duration;

public interface ExpirationScheduler {

    Runnable scheduleAtFixedRate(final Runnable task, final Duration initialDelay, final Duration period);

    void shutdown();
}
