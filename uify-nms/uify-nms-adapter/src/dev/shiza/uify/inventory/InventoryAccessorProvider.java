package dev.shiza.uify.inventory;

import dev.shiza.uify.initialization.Lazy;
import dev.shiza.uify.version.ServerVersion;
import dev.shiza.uify.version.ServerVersionProvider;
import dev.shiza.uify.version.UnsupportedServerVersionException;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class InventoryAccessorProvider {

    private static final Lazy<InventoryAccessor> INVENTORY_ACCESSOR =
        Lazy.lazy(InventoryAccessorProvider::inventoryAccessor0);

    private InventoryAccessorProvider() {
    }

    public static InventoryAccessor inventoryAccessor() {
        return INVENTORY_ACCESSOR.get();
    }

    private static InventoryAccessor inventoryAccessor0() {
        final ServerVersion serverVersion = ServerVersionProvider.getServerVersion();
        if (serverVersion.equals(ServerVersion.V1_21_4)) {
            return new InventoryAccessorV1_21_4();
        } else if (serverVersion.equals(ServerVersion.V1_20_4)) {
            return new InventoryAccessorV1_20_4();
        } else {
            throw new UnsupportedServerVersionException(
                "Could not find inventory accessor implementation for current version.");
        }
    }
}
