package dev.shiza.uify.canvas.element.cooldown.expiration;

public interface ExpirationScheduler {

    Runnable scheduleAtFixedRate(final Runnable task, final int initialDelay, final int period);

    void shutdown();
}
