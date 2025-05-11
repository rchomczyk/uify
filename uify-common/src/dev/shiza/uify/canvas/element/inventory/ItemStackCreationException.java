package dev.shiza.uify.canvas.element.inventory;

public final class ItemStackCreationException extends IllegalStateException {

    public ItemStackCreationException(final String message) {
        super(message);
    }

    public ItemStackCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
