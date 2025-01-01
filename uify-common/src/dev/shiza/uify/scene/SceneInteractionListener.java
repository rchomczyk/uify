package dev.shiza.uify.scene;

import dev.shiza.uify.scene.inventory.SceneInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class SceneInteractionListener implements Listener {

    @EventHandler
    public void delegateInventoryClose(final InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();
        if (event.getPlayer() instanceof Player player
            && inventory.getHolder() instanceof SceneInventoryHolder sceneInventoryHolder
            && sceneInventoryHolder.sceneMorph() instanceof SceneImpl scene) {
            scene.sceneCloseBehaviour().accept(player);
        }
    }
}
