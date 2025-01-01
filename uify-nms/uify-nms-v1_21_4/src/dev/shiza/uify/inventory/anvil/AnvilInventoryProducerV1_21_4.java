package dev.shiza.uify.inventory.anvil;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

final class AnvilInventoryProducerV1_21_4 implements AnvilInventoryProducer {

    @Override
    public AnvilInventory produce(final Player player, final Component title) {
        return new AnvilInventoryV1_21_4(player, title);
    }
}
