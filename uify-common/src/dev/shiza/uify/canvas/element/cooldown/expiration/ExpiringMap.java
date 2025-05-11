package dev.shiza.uify.canvas.element.cooldown.expiration;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class ExpiringMap<K, V> {

    private final Map<K, ExpiringEntry<V>> underlyingMap;
    private final Duration defaultTtl;
    private final ExpirationScheduler scheduler;
    private final Runnable scheduledTask;
    private final Set<ExpirationListener<K, V>> expirationListeners;

    public ExpiringMap(
        final Duration defaultTtl,
        final boolean threadSafe,
        final Duration cleanupInterval,
        final ExpirationScheduler scheduler) {
        this.defaultTtl = defaultTtl;
        this.underlyingMap = threadSafe ? new ConcurrentHashMap<>() : new HashMap<>();
        this.expirationListeners = threadSafe ?
            Collections.newSetFromMap(new ConcurrentHashMap<>()) : new HashSet<>();
        if (cleanupInterval.compareTo(Duration.ZERO) > 0) {
            this.scheduler = scheduler != null ? scheduler : new DefaultExpirationScheduler();
            this.scheduledTask =
                this.scheduler.scheduleAtFixedRate(this::removeExpiredEntries, cleanupInterval, cleanupInterval);
        } else {
            this.scheduler = null;
            this.scheduledTask = null;
        }
    }

    public ExpiringMap(final Duration defaultTtl, final Duration cleanupInterval) {
        this(defaultTtl, true, cleanupInterval, null);
    }

    public ExpiringMap(
        final Duration defaultTtl, final Duration cleanupInterval, final ExpirationScheduler scheduler) {
        this(defaultTtl, true, cleanupInterval, scheduler);
    }

    public ExpiringMap(final Duration defaultTtl) {
        this(defaultTtl, true, Duration.ZERO, null);
    }

    public void expirationListener(final ExpirationListener<K, V> listener) {
        expirationListeners.add(listener);
    }

    private void notifyListeners(final K key, final V value) {
        for (final ExpirationListener<K, V> listener : expirationListeners) {
            try {
                listener.onExpire(key, value);
            } catch (Exception e) {
                throw new ExpirationListenerNotifyingException("Could not notify listeners about entry expiration.");
            }
        }
    }

    public V put(final K key, final V value) {
        return put(key, value, defaultTtl);
    }

    public V put(final K key, final V value, final Duration ttl) {
        final ExpiringEntry<V> newEntry = new ExpiringEntry<>(value, Instant.now().plus(ttl));
        final ExpiringEntry<V> oldEntry = underlyingMap.put(key, newEntry);
        return oldEntry != null ? oldEntry.value() : null;
    }

    public V get(final K key) {
        final ExpiringEntry<V> entry = underlyingMap.get(key);
        if (entry == null) {
            return null;
        }

        if (entry.isExpired()) {
            final V expiredValue = entry.value();
            underlyingMap.remove(key);
            notifyListeners(key, expiredValue);
            return null;
        }

        return entry.value();
    }

    public V remove(final K key) {
        final ExpiringEntry<V> entry = underlyingMap.remove(key);
        return entry != null ? entry.value() : null;
    }

    public boolean containsKey(final K key) {
        return underlyingMap.containsKey(key);
    }

    public int size() {
        return underlyingMap.size();
    }

    public void removeExpiredEntries() {
        final Map<K, V> expiredEntries = new HashMap<>();
        for (final Map.Entry<K, ExpiringEntry<V>> entry : new ArrayList<>(underlyingMap.entrySet())) {
            final K key = entry.getKey();

            final ExpiringEntry<V> value = entry.getValue();
            if (value.isExpired()) {
                expiredEntries.put(key, value.value());
            }
        }

        for (final Map.Entry<K, V> expired : expiredEntries.entrySet()) {
            underlyingMap.remove(expired.getKey());
            notifyListeners(expired.getKey(), expired.getValue());
        }
    }

    public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final V oldValue = get(key);
        final V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            if (oldValue != null) {
                remove(key);
            }

            return null;
        } else {
            put(key, newValue);
            return newValue;
        }
    }

    public void clear() {
        underlyingMap.clear();
    }

    public void shutdown() {
        if (scheduler != null) {
            if (scheduledTask != null) {
                scheduledTask.run();
            }

            scheduler.shutdown();
        }
    }
}