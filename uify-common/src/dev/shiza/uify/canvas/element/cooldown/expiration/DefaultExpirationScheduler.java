package dev.shiza.uify.canvas.element.cooldown.expiration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

final class DefaultExpirationScheduler implements ExpirationScheduler {

    private final ScheduledExecutorService executor;

    public DefaultExpirationScheduler() {
        this.executor = Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());
    }

    @Override
    public Runnable scheduleAtFixedRate(final Runnable task, final int initialDelay, final int period) {
        final ScheduledFuture<?> future = executor.scheduleAtFixedRate(
            task, initialDelay, period, TimeUnit.SECONDS);
        return () -> future.cancel(false);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}

