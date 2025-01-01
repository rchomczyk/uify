package dev.shiza.uify.inventory.anvil;

import dev.shiza.uify.initialization.Lazy;
import dev.shiza.uify.version.ServerVersion;
import dev.shiza.uify.version.ServerVersionProvider;
import dev.shiza.uify.version.UnsupportedServerVersionException;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class AnvilInventoryFactory {

    private static final Lazy<AnvilInventoryProducer> ANVIL_INVENTORY_PRODUCER =
        Lazy.lazy(AnvilInventoryFactory::anvil0);

    private AnvilInventoryFactory() {}

    public static AnvilInventory anvilInventory(final Player player, final Component title) {
        return ANVIL_INVENTORY_PRODUCER.get().produce(player, title);
    }

    private static AnvilInventoryProducer anvil0() {
        final ServerVersion serverVersion = ServerVersionProvider.getServerVersion();
        if (serverVersion.equals(ServerVersion.V1_21_4)) {
            return new AnvilInventoryProducerV1_21_4();
        } else if (serverVersion.equals(ServerVersion.V1_20_4)) {
            return new AnvilInventoryProducerV1_20_4();
        } else {
            throw new UnsupportedServerVersionException(
                "Could not find anvil inventory implementation for current version.");
        }
    }
}
