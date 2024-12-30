package dev.shiza.uify.inventory;

import dev.shiza.uify.initialization.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public final class InventoryAccessorProvider {

    private static final String V1_21_4 = "1.21.4";
    private static final String V1_20_4 = "1.20.4";
    private static final String VERSION_SEGMENT_DELIMITER = "-";
    private static final Lazy<InventoryAccessor> INVENTORY_ACCESSOR =
        Lazy.lazy(InventoryAccessorProvider::inventoryAccessor0);

    private InventoryAccessorProvider() {
    }

    public static InventoryAccessor inventoryAccessor() {
        return INVENTORY_ACCESSOR.get();
    }

    private static InventoryAccessor inventoryAccessor0() {
        final Server server = Bukkit.getServer();
        final String version = server.getBukkitVersion();
        return switch (version.substring(0, version.indexOf(VERSION_SEGMENT_DELIMITER))) {
            case V1_21_4:
                yield new InventoryAccessorV1_21_4();
            case V1_20_4:
                yield new InventoryAccessorV1_20_4();
            default:
                throw new IllegalArgumentException("Unsupported version: " + version);
        };
    }
}
