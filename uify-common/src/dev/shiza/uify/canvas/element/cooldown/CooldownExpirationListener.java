package dev.shiza.uify.canvas.element.cooldown;

import dev.shiza.uify.canvas.element.behaviour.cooldown.CooldownGenericBehaviourState;
import dev.shiza.uify.canvas.element.cooldown.expiration.ExpirationListener;
import dev.shiza.uify.inventory.InventoryEvents;
import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.bukkit.entity.HumanEntity;

final class CooldownExpirationListener implements ExpirationListener<CooldownCompositeKey, Instant> {

    @Override
    public void onExpire(final CooldownCompositeKey compositeKey, final Instant expirationTime) {
        final CanvasBaseElement element = (CanvasBaseElement) compositeKey.element();

        final Optional<Canvas> maybeOwner = element.owners().stream().findFirst();
        if (maybeOwner.isEmpty()) {
            return;
        }
        final BaseCanvas owner = (BaseCanvas) maybeOwner.get();

        final Optional<SceneInventoryHolder> maybeHolder = owner.owners().stream().findFirst();
        if (maybeHolder.isEmpty()) {
            return;
        }
        final SceneInventoryHolder holder = maybeHolder.get();

        for (final HumanEntity viewer : holder.getInventory().getViewers()) {
            if (viewer.getUniqueId().equals(compositeKey.viewerUniqueId())) {
                element.elementCooldownExpirationBehaviour()
                    .accept(
                        new CooldownGenericBehaviourState<>(holder, owner, element, Duration.ZERO),
                        InventoryEvents.dummyClickEvent(viewer));
            }
        }
    }
}
