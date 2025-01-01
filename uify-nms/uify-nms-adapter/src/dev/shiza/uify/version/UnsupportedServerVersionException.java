package dev.shiza.uify.version;

public final class UnsupportedServerVersionException extends IllegalStateException {

    public UnsupportedServerVersionException(final String message) {
        super(message);
    }
}
