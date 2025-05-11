package dev.shiza.uify.canvas.element.cooldown.expiration;

public interface ExpirationListener<K, V> {

    void onExpire(final K key, final V value);
}
