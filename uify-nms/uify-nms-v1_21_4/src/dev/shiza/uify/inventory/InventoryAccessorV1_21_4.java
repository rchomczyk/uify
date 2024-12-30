package dev.shiza.uify.inventory;

import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

final class InventoryAccessorV1_21_4 implements InventoryAccessor {

    InventoryAccessorV1_21_4() {}

    @Override
    public void title(final InventoryHolder holder, final Component title) {
        final Inventory inventory = holder.getInventory();
        if (inventory.getViewers().isEmpty()) {
            return;
        }

        for (final HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player player) {
                sendTitleChangePacket(player, title);
            }
        }
    }

    private void sendTitleChangePacket(final Player player, final Component title) {
        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final ServerPlayer serverPlayer = craftPlayer.getHandle();
        serverPlayer.connection.send(
            new ClientboundOpenScreenPacket(
                serverPlayer.containerMenu.containerId,
                serverPlayer.containerMenu.getType(),
                new AdventureComponent(title)));
    }
}