package dev.shiza.uify.inventory.anvil;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface AnvilInventoryProducer {

    AnvilInventory produce(final Player player, final Component title);
}
