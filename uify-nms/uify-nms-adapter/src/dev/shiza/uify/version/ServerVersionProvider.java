package dev.shiza.uify.version;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ServerVersionProvider {

    private static final String V1_21_4 = "1.21.4";
    private static final String V1_20_4 = "1.20.4";
    private static final String VERSION_SEGMENT_DELIMITER = "-";

    private ServerVersionProvider() {}

    public static ServerVersion getServerVersion() {
        final Server server = Bukkit.getServer();
        final String version = server.getBukkitVersion();
        return switch (version.substring(0, version.indexOf(VERSION_SEGMENT_DELIMITER))) {
            case V1_21_4:
                yield ServerVersion.V1_21_4;
            case V1_20_4:
                yield ServerVersion.V1_20_4;
            default:
                throw new IllegalArgumentException("Unsupported version: " + version);
        };
    }
}
