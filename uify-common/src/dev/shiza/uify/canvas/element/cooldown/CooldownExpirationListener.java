package dev.shiza.uify.canvas.element.cooldown;

import dev.shiza.uify.canvas.BaseCanvas;
import dev.shiza.uify.canvas.Canvas;
import dev.shiza.uify.canvas.element.CanvasBaseElement;
import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import net.jodah.expiringmap.ExpirationListener;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

final class CooldownExpirationListener implements ExpirationListener<CooldownCompositeKey, Instant> {

    @Override
    public void expired(final CooldownCompositeKey compositeKey, final Instant expirationTime) {
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
                        dummyEvent(viewer));
            }
        }
    }

    private InventoryClickEvent dummyEvent(final HumanEntity viewer) {
        return new InventoryClickEvent(
            viewer.getOpenInventory(), InventoryType.SlotType.OUTSIDE, 999, ClickType.LEFT, InventoryAction.NOTHING);
    }
}
