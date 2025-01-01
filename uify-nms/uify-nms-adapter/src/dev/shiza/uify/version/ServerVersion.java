package dev.shiza.uify.version;

public record ServerVersion(String version) {
    public static final ServerVersion V1_21_4 = new ServerVersion("1.21.4");
    public static final ServerVersion V1_20_4 = new ServerVersion("1.20.4");
}
